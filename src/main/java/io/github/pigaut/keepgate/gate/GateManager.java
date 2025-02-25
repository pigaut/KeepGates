package io.github.pigaut.keepgate.gate;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.structure.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GateManager extends Manager {

    private final KeepGatePlugin plugin;
    private final List<Gate> gates = new ArrayList<>();
    private final Map<Location, Gate> generatorBlocks = new ConcurrentHashMap<>();

    public GateManager(KeepGatePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        for (Gate blockGenerator : gates) {
            blockGenerator.cancelTransition();
            for (Block block : blockGenerator.getAllOccupiedBlocks()) {
                block.setType(Material.AIR);
            }
            final HologramDisplay hologramDisplay = blockGenerator.getCurrentHologram();
            if (hologramDisplay != null) {
                hologramDisplay.despawn();
            }
        }
    }

    @Override
    public void loadData() {
        gates.clear();
        generatorBlocks.clear();
        final DataTable resourcesTable = plugin.getDatabase().tableOf("gates");
        resourcesTable.createIfNotExists(
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        resourcesTable.selectAll().fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            final World world = Bukkit.getWorld(UUID.fromString(worldId));
            if (world == null) {
                plugin.getLogger().warning("Removed gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: world does not exist.");
                return;
            }

            final GateTemplate template = plugin.getGateTemplate(generatorName);
            if (template == null) {
                plugin.getLogger().warning("Removed gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: gate does not exist.");
                return;
            }

            final Location origin = new Location(world, x, y, z);
            final Rotation rotation = Deserializers.getEnum(Rotation.class, rotationData);

            if (rotation == null) {
                plugin.getLogger().warning("Removed gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: invalid rotation data.");
                return;
            }

            final int maxStage = template.getMaxStage();
            if (stage > maxStage) {
                plugin.getLogger().warning("Failed to load saved gate stage. Reason: " + template.getName() +
                        " gate supports a maximum of " + maxStage + " stages.");
            }

            plugin.getScheduler().runTask(() -> {
                try {
                    Gate.create(template, origin, rotation, Math.min(stage, maxStage));
                } catch (GateOverlapException e) {
                    plugin.getLogger().warning("Removed gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                            "Reason: gates overlapped.");
                }
            });
        });
    }

    @Override
    public void saveData() {
        final DataTable resourcesTable = plugin.getDatabase().tableOf("gates");
        resourcesTable.createIfNotExists(
                "world VARCHAR(36)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5)",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.clear();
        final DatabaseStatement insertStatement =
                resourcesTable.insertInto("world", "x", "y", "z", "gate", "rotation", "stage");
        for (Gate gate : gates) {
            final Location location = gate.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(gate.getTemplate().getName());
            insertStatement.withParameter(gate.getRotation().toString());
            insertStatement.withParameter(gate.getCurrentStageId());
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    public Collection<Gate> getAllGenerators() {
        return new ArrayList<>(gates);
    }

    public boolean isGate(@NotNull Location location) {
        return generatorBlocks.containsKey(location);
    }

    public @Nullable Gate getGenerator(@NotNull Location location) {
        return generatorBlocks.get(location);
    }

    public void registerGate(@NotNull Gate generator) {
        gates.add(generator);
        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.put(block.getLocation(), generator);
        }
    }

    public void removeGate(@NotNull Gate generator) {
        gates.remove(generator);
        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.remove(block.getLocation());
        }
        final BlockStructure structure = generator.getCurrentStage().getStructure();
        structure.removeBlocks(generator.getOrigin(), generator.getRotation());

        final HologramDisplay hologram = generator.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.despawn();
        }
    }

}
