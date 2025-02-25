package io.github.pigaut.keepgate.gate;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.event.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.stage.*;
import io.github.pigaut.keepgate.structure.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Gate implements PlaceholderSupplier {

    private static final KeepGatePlugin plugin = KeepGatePlugin.getPlugin();

    private final GateTemplate template;
    private final Location origin;
    private final Rotation rotation;
    private int currentStage;
    private @Nullable BukkitTask transitionTask = null;
    private @Nullable HologramDisplay currentHologram = null;
    private BlockStructure currentStructure;
    private GateState state = GateState.CLOSED;

    private Gate(GateTemplate generator, Location origin, int currentStage, Rotation rotation) {
        this.template = generator;
        this.origin = origin.clone();
        this.currentStage = currentStage;
        this.currentStructure = generator.getStage(currentStage).getStructure();
        this.rotation = rotation;
    }

    public static @NotNull Gate create(@NotNull GateTemplate template, @NotNull Location origin, Rotation rotation, int stage) throws GateOverlapException {
        for (Block block : template.getAllOccupiedBlocks(origin, rotation)) {
            if (plugin.getGates().isGate(block.getLocation())) {
                throw new GateOverlapException();
            }
        }
        final Gate gate = new Gate(template, origin, stage, rotation);
        plugin.getGates().registerGate(gate);
        gate.updateState();
        return gate;
    }

    public static @NotNull Gate create(@NotNull GateTemplate template, @NotNull Location origin, Rotation rotation) throws GateOverlapException {
        return create(template, origin, rotation, template.getMaxStage());
    }

    public static @NotNull Gate create(@NotNull GateTemplate template, @NotNull Location origin) throws GateOverlapException {
        return create(template, origin, Rotation.NONE);
    }

    public @NotNull GateTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin.clone();
    }

    public Set<Block> getAllOccupiedBlocks() {
        return template.getAllOccupiedBlocks(origin, rotation);
    }

    public boolean matchBlocks() {
        return getCurrentStage().getStructure().matchBlocks(origin, rotation);
    }

    public List<Block> getBlocks() {
        return getCurrentStage().getStructure().getBlocks(origin, rotation);
    }

    public void removeBlocks() {
        getCurrentStage().getStructure().removeBlocks(origin, rotation);
    }

    public boolean exists() {
        for (Block block : getBlocks()) {
            if (!plugin.getGates().isGate(block.getLocation())) {
                return false;
            }
        }
        return true;
    }

    public boolean isFirstStage() {
        return currentStage == 0;
    }

    public boolean isLastStage() {
        return currentStage >= template.getMaxStage();
    }

    public int getCurrentStageId() {
        return currentStage;
    }

    public @NotNull GateStage getCurrentStage() {
        return template.getStage(currentStage);
    }

    public void setCurrentStage(int stage) {
        if (!exists()) {
            return;
        }

        plugin.getScheduler().runTaskLater(1, () -> {
            final BlockStructure nextStructure = template.getStage(stage).getStructure();
            for (Block previousBlock : template.getAllOccupiedBlocks(origin, rotation)) {
                final Block nextBlock = nextStructure.getBlockAt(origin, rotation, previousBlock.getLocation());
                if (nextBlock == null || nextBlock.getType() != previousBlock.getType()) {
                    previousBlock.setType(Material.AIR, false);
                }
            }

            this.currentStage = stage;
            updateState();
        });
    }

    public Rotation getRotation() {
        return rotation;
    }

    public @Nullable HologramDisplay getCurrentHologram() {
        return currentHologram;
    }

    public void cancelTransition() {
        if (transitionTask != null) {
            transitionTask.cancel();
        }
        transitionTask = null;
    }

    public void open() {
        if (isLastStage()) {
            throw new IllegalStateException("Block generator does not have a next stage");
        }

        state = GateState.OPENING;
        final int peekStage = currentStage + 1;
        final GateStage nextStage = template.getStage(peekStage);

        final Function growthFunction = nextStage.getTransitionFunction();
        if (growthFunction != null) {
            growthFunction.run(origin.getBlock());
        }

        setCurrentStage(peekStage);
    }

    public void close() {
        if (isFirstStage()) {
            throw new IllegalStateException("Block generator does not have a previous stage");
        }

        state = GateState.CLOSING;
        final int peekStage = currentStage - 1;
        final GateStage previousStage = template.getStage(peekStage);

        final Function growthFunction = previousStage.getTransitionFunction();
        if (growthFunction != null) {
            growthFunction.run(origin.getBlock());
        }

        setCurrentStage(currentStage - 1);
    }

    private void updateState() {
        final GateStage stage = getCurrentStage();
        stage.getStructure().updateBlocks(origin, rotation);

        if (currentHologram != null) {
            currentHologram.despawn();
            currentHologram = null;
        }

        if (state == GateState.OPENING && !isLastStage()) {
            if (transitionTask != null && !transitionTask.isCancelled()) {
                transitionTask.cancel();
            }

            transitionTask = plugin.getScheduler().runTaskLater(stage.getDelay(), () -> {
                transitionTask = null;
                final GateTransitionEvent transitionEvent = new GateTransitionEvent(this);
                SpigotServer.callEvent(transitionEvent);
                if (!transitionEvent.isCancelled()) {
                    this.open();
                }
            });
        }

        if (state == GateState.CLOSING && !isFirstStage()) {
            if (transitionTask != null && !transitionTask.isCancelled()) {
                transitionTask.cancel();
            }

            transitionTask = plugin.getScheduler().runTaskLater(stage.getDelay(), () -> {
                transitionTask = null;
                final GateTransitionEvent transitionEvent = new GateTransitionEvent(this);
                SpigotServer.callEvent(transitionEvent);
                if (!transitionEvent.isCancelled()) {
                    this.close();
                }
            });
        }

        if (state.isTransition()) {
            state = state == GateState.OPENING ? GateState.OPENED : GateState.CLOSED;
        }

        final Hologram hologram = stage.getHologram();
        if (hologram != null) {
            final Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            currentHologram = hologram.spawn(offsetLocation, rotation, false, this);
        }
    }

    @Override
    public String toString() {
        return "Gate{" +
                "template=" + template +
                ", origin=" + origin +
                ", rotation=" + rotation +
                ", currentStage=" + currentStage +
                ", transitionTask=" + transitionTask +
                ", currentHologram=" + currentHologram +
                ", currentStructure=" + currentStructure +
                ", state=" + state +
                '}';
    }

    @Override
    public @NotNull Placeholder[] getPlaceholders() {
        return new Placeholder[] {
                Placeholder.of("%gate_world%", origin.getWorld().getName()),
                Placeholder.of("%gate_x%", origin.getBlockX()),
                Placeholder.of("%gate_y%", origin.getBlockY()),
                Placeholder.of("%gate_z%", origin.getBlockZ())
        };
    }

}
