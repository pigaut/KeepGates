package io.github.pigaut.keepgate.item;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ToolManager extends Manager {

    private ItemStack generatorTool;
    private ItemStack wandTool;

    public ToolManager(EnhancedPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadData() {
        super.loadData();
        final ConfigSection config = plugin.getConfiguration();
        generatorTool = config.get("generator-tool", ItemStack.class);
        wandTool = config.get("wand-tool", ItemStack.class);
    }

    public @NotNull ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

    public void setGeneratorTool(@NotNull ItemStack generatorTool) {
        this.generatorTool = generatorTool;
    }

    public @NotNull ItemStack getWandTool() {
        return wandTool.clone();
    }

    public void setWandTool(@NotNull ItemStack wandTool) {
        this.wandTool = wandTool;
    }
}
