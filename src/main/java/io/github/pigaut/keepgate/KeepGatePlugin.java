package io.github.pigaut.keepgate;

import io.github.pigaut.keepgate.command.*;
import io.github.pigaut.keepgate.config.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.item.*;
import io.github.pigaut.keepgate.listener.*;
import io.github.pigaut.keepgate.player.*;
import io.github.pigaut.keepgate.structure.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class KeepGatePlugin extends EnhancedJavaPlugin {

    private final ToolManager toolManager = new ToolManager(this);
    private final StructureManager structureManager = new StructureManager(this);
    private final GateTemplateManager templateManager = new GateTemplateManager(this);
    private final GateManager generatorManager = new GateManager(this);
    private final OrestackPlayerManager playerManager = new OrestackPlayerManager(this);
    private final Database database = SQLib.createDatabase(new File(("plugins/KeepGate/data")));

    private static KeepGatePlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

//    @Override
//    public void onDisable() {
//        super.onDisable();
//        database.closeConnection();
//    }

    public static KeepGatePlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_16_5);
    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24908;
    }

    @Override
    public @Nullable Integer getResourceId() {
        return null;
    }

    @Override
    public @Nullable String getDonationLink() {
        return "https://www.paypal.com/paypalme/Giovanni335";
    }

    @Override
    public List<String> getPluginResources() {
        return List.of("config.yml",
                "flags.yml",
                "languages/en.yml"
        );
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "gates", "messages", "languages", "structures", "effects/particles", "effects/sounds");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "items/items.yml",
                "messages/messages.yml",
                "effects/particles/particles.yml",
                "effects/sounds/sounds.yml",

                "gates/simple.yml",
                "gates/grate.yml",

                "structures/simple/simple_closed.yml",
                "structures/simple/simple_transition.yml",
                "structures/simple/simple_opened.yml",

                "structures/grate/grate_closed.yml",
                "structures/grate/grate_transition_1.yml",
                "structures/grate/grate_transition_2.yml",
                "structures/grate/grate_opened.yml"
        );
    }

    @Override
    public List<EnhancedCommand> getPluginCommands() {
        return List.of(new KeepGateCommand(this));
    }

    @Override
    public List<Listener> getPluginListeners() {
        return List.of(
                new PlayerInteractListener(plugin),
                new BlockBreakListener(plugin),
                new BlockDestructionListener(plugin),
                new CropChangeListener(plugin),
                new ChunkLoadListener(plugin)
        );
    }

    public ToolManager getTools() {
        return toolManager;
    }

    public @NotNull StructureManager getStructures() {
        return structureManager;
    }

    public @Nullable BlockStructure getBlockStructure(String name) {
        return structureManager.getBlockStructure(name);
    }

    public @NotNull GateTemplateManager getGateTemplates() {
        return templateManager;
    }

    public @Nullable GateTemplate getGateTemplate(String name) {
        return templateManager.getGateTemplate(name);
    }

    public @NotNull GateManager getGates() {
        return generatorManager;
    }

    public @Nullable Gate getGate(@NotNull Location location) {
        return generatorManager.getGenerator(location);
    }

    @Override
    public @NotNull PlayerManager<KeepGatePlayer> getPlayers() {
        return playerManager;
    }

    @Override
    public @Nullable KeepGatePlayer getPlayer(String playerName) {
        return playerManager.getPlayer(playerName);
    }

    @Override
    public @Nullable KeepGatePlayer getPlayer(UUID playerId) {
        return playerManager.getPlayer(playerId);
    }

    @Override
    public @NotNull KeepGateConfigurator getConfigurator() {
        return new KeepGateConfigurator(this);
    }

    public Database getDatabase() {
        return database;
    }

}
