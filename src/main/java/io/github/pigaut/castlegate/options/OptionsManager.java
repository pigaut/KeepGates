package io.github.pigaut.castlegate.options;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class OptionsManager extends Manager {

    private ItemStack gateTool;

    public OptionsManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadData() {
        super.loadData();
        final ConfigSection config = plugin.getConfiguration();
        gateTool = config.get("gate-tool", ItemStack.class);
    }

    public @NotNull ItemStack getGateTool() {
        return gateTool.clone();
    }

}
