package io.github.pigaut.castlegate.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GateManager extends Manager {

    private final GatePlugin plugin;
    private final List<Gate> gates = new ArrayList<>();
    private final Map<Location, Gate> gateBlocks = new ConcurrentHashMap<>();

    public GateManager(GatePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        for (Gate gate : gates) {
            gate.cancelTransition();
            for (Block block : gate.getAllOccupiedBlocks()) {
                block.setType(Material.AIR, false);
            }
            final HologramDisplay hologramDisplay = gate.getCurrentHologram();
            if (hologramDisplay != null) {
                hologramDisplay.despawn();
            }
        }
    }

    @Override
    public void loadData() {
        gates.clear();
        gateBlocks.clear();

        final Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getLogger().severe("Could not load data because database was not found.");
            return;
        }

        database.createTableIfNotExists("gates",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        database.selectAll("gates").fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String gateName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            final World world = Bukkit.getWorld(UUID.fromString(worldId));
            if (world == null) {
                logger.warning("Ignored gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: world does not exist.");
                return;
            }

            final GateTemplate template = plugin.getGateTemplate(gateName);
            if (template == null) {
                logger.warning("Ignored gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: gate template does not exist.");
                return;
            }

            final Location origin = new Location(world, x, y, z);
            Rotation rotation = Deserializers.getEnum(Rotation.class, rotationData);
            if (rotation == null) {
                rotation = Rotation.NONE;
                logger.warning("Reset gate rotation at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: invalid rotation data.");
            }

            final int maxStage = template.getMaxStage();
            if (stage > maxStage) {
                logger.warning("Reset gate stage at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: stage data surpasses the current max stage.");
            }

            final Rotation finalRotation = rotation;
            plugin.getScheduler().runTask(() -> {
                try {
                    Gate.create(template, origin, finalRotation, Math.min(stage, maxStage));
                } catch (GateOverlapException e) {
                    plugin.getLogger().warning("Removed gate at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                            "Reason: gates overlapped.");
                }
            });
        });
    }

    @Override
    public void saveData() {
        final Database database = plugin.getDatabase();
        if (database == null) {
            logger.severe("Could not save data because database was not found.");
            return;
        }

        database.createTableIfNotExists("gates",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        final DatabaseStatement insertStatement = database.merge("gates", "world, x, y, z",
                "world", "x", "y", "z", "gate", "rotation", "stage");

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

    public Collection<Gate> getAllGates() {
        return new ArrayList<>(gates);
    }

    public boolean isGate(@NotNull Location location) {
        return gateBlocks.containsKey(location);
    }

    public @Nullable Gate getGate(@NotNull Location location) {
        return gateBlocks.get(location);
    }

    public void registerGate(@NotNull Gate gate) {
        gates.add(gate);
        for (Block block : gate.getAllOccupiedBlocks()) {
            gateBlocks.put(block.getLocation(), gate);
        }
    }

    public void unregisterGate(@NotNull Gate gate) {
        gates.remove(gate);
        for (Block block : gate.getAllOccupiedBlocks()) {
            gateBlocks.remove(block.getLocation());
        }
        final BlockStructure structure = gate.getCurrentStage().getStructure();
        structure.removeBlocks(gate.getOrigin(), gate.getRotation());

        final HologramDisplay hologram = gate.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.despawn();
        }
    }

}
