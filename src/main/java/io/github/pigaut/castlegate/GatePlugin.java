package io.github.pigaut.castlegate;

import io.github.pigaut.castlegate.command.*;
import io.github.pigaut.castlegate.config.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.listener.*;
import io.github.pigaut.castlegate.options.*;
import io.github.pigaut.castlegate.player.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GatePlugin extends EnhancedJavaPlugin {

    private final OptionsManager toolManager = new OptionsManager(this);
    private final GateTemplateManager gateTemplateManager = new GateTemplateManager(this);
    private final GateManager gateManager = new GateManager(this);
    private final GatePlayerManager playerManager = new GatePlayerManager(this);

    private static GatePlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static GatePlugin getPlugin() {
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
        return 122811;
    }

    @Override
    public @Nullable String getDonationLink() {
        return "https://www.paypal.com/paypalme/Giovanni335";
    }

    @Override
    public List<String> getPluginResources() {
        return List.of("config.yml",
                "languages/en.yml"
        );
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "gates", "messages", "languages", "structures", "effects/particles", "effects/sounds", "functions");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "items/misc.yml",
                "messages/misc.yml",
                "functions/misc.yml",
                "effects/particles/misc.yml",
                "effects/particles/flame.yml",
                "effects/sounds/misc.yml",

                "gates/simple.yml",
                "gates/simple_auto.yml",
                "gates/grate.yml",

                "gates/locked/simple_locked.yml",

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
        return List.of(new CastleGateCommand(this));
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

    public OptionsManager getTools() {
        return toolManager;
    }

    public @NotNull GateTemplateManager getGateTemplates() {
        return gateTemplateManager;
    }

    public @Nullable GateTemplate getGateTemplate(String name) {
        return gateTemplateManager.get(name);
    }

    public @NotNull GateManager getGates() {
        return gateManager;
    }

    public @Nullable Gate getGate(@NotNull Location location) {
        return gateManager.getGate(location);
    }

    @Override
    public @NotNull GatePlayerManager getPlayersState() {
        return playerManager;
    }

    @Override
    public @NotNull GatePlayer getPlayerState(@NotNull Player player) {
        return playerManager.getPlayerState(player);
    }

    @Override
    public @Nullable GatePlayer getPlayerState(@NotNull String playerName) {
        return playerManager.getPlayerState(playerName);
    }

    @Override
    public @Nullable GatePlayer getPlayerState(@NotNull UUID playerId) {
        return playerManager.getPlayerState(playerId);
    }

    @Override
    public @NotNull GateConfigurator getConfigurator() {
        return new GateConfigurator(this);
    }

}
