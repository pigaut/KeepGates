package io.github.pigaut.castlegate.gate.template;

import io.github.pigaut.castlegate.stage.*;
import io.github.pigaut.castlegate.util.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateTemplate implements Identifiable, PlaceholderSupplier {

    private final String name;
    private final String group;
    private final List<GateStage> stages;
    private Rotation rotation = Rotation.NONE;

    public GateTemplate(@NotNull String name, @Nullable String group, List<GateStage> stages) {
        this.name = name;
        this.group = group;
        this.stages = stages;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public ItemStack getItem() {
        return GateTools.getGateTool(this);
    }

    public int getMaxStage() {
        return stages.size() - 1;
    }

    public List<GateStage> getStages() {
        return new ArrayList<>(stages);
    }

    public GateStage getStage(int stage) {
        return stages.get(stage);
    }

    public GateStage getLastStage() {
        return stages.get(getMaxStage());
    }

    public int getStageFromStructure(Location origin, Rotation rotation) {
        int currentStage = getMaxStage();
        for (int i = getMaxStage(); i >= 0; i--) {
            final GateStage stage = getStage(i);
            if (stage.getStructure().matchBlocks(origin, rotation)) {
                currentStage = i;
                break;
            }
        }
        return currentStage;
    }

    public Set<Block> getAllOccupiedBlocks(Location location, Rotation rotation) {
        final Set<Block> blocks = new HashSet<>();
        for (GateStage stage : stages) {
            blocks.addAll(stage.getStructure().getBlocks(location, rotation));
        }
        return blocks;
    }

    public Placeholder[] getPlaceholders() {
        return new Placeholder[]{
                Placeholder.of("{gate}", name),
                Placeholder.of("{gate_stages}", stages),
                Placeholder.of("{gate_rotation}", rotation)
        };
    }

    @Override
    public String toString() {
        return "Gate{" + "name='" + name + '\'' + ", stages=" + stages + '}';
    }

}
