package io.github.pigaut.keepgate.action;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class CloseGateAction implements BlockAction {

    private final KeepGatePlugin plugin = KeepGatePlugin.getPlugin();

    @Override
    public void execute(@NotNull Block block) {
        final Gate generator = plugin.getGate(block.getLocation());
        if (generator != null && !generator.isFirstStage()) {
            generator.close();
        }
    }

}
