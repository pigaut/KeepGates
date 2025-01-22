package io.github.pigaut.orestack;

import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.external.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OrestackPlayerManager playerManager = new OrestackPlayerManager();
    private final Database database = SQLib.createDatabase(getFile("data.db"));

    private static OrestackPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static OrestackPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_17);
    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24502;
    }

    @Override
    public @Nullable Integer getResourceId() {
        return 91628;
    }

    @Override
    public @Nullable String getDonationLink() {
        return "https://www.paypal.com/paypalme/Giovanni335";
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "generators", "messages", "languages", "effects/particles", "effects/sounds");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "config.yml",
                "languages/en.yml",
                "generators/example.yml",
                "generators/crops/wheat.yml",
                "generators/crops/potato.yml",
                "generators/crops/carrot.yml",
                "generators/crops/beetroot.yml",
                "generators/crops/melon.yml",
                "generators/crops/pumpkin.yml",
                "generators/crops/cocoa/cocoa_north.yml",
                "generators/ores/coal.yml",
                "generators/ores/iron.yml",
                "generators/ores/gold.yml",
                "generators/ores/diamond.yml",
                "items/items.yml",
                "messages/messages.yml",
                "effects/particles/particles.yml",
                "effects/sounds/sounds.yml",
                "flags.yml"
        );
    }

    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    public @Nullable Generator getGenerator(String name) {
        return generatorManager.getGenerator(name);
    }

    public @Nullable Generator getGenerator(ItemStack item) {
        final String generatorName = GeneratorItem.getGeneratorFromItem(item);
        return getGenerator(generatorName);
    }

    public @Nullable BlockGenerator getBlockGenerator(@NotNull Location location) {
        return generatorManager.getBlockGenerator(location);
    }

    @Override
    public @NotNull PlayerManager<OrestackPlayer> getPlayers() {
        return playerManager;
    }

    @Override
    public @Nullable OrestackPlayer getPlayer(String playerName) {
        return playerManager.getPlayer(playerName);
    }

    @Override
    public @Nullable OrestackPlayer getPlayer(UUID playerId) {
        return playerManager.getPlayer(playerId);
    }

    @Override
    public @NotNull OrestackConfigurator getConfigurator() {
        return new OrestackConfigurator(this);
    }

    public Database getDatabase() {
        return database;
    }

}
