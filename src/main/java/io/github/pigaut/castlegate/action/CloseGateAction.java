package io.github.pigaut.castlegate.action;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.voxel.core.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class CloseGateAction implements BlockAction {

    private final GatePlugin plugin = GatePlugin.getPlugin();

    @Override
    public void execute(@NotNull Block block) {
        final Gate gate = plugin.getGate(block.getLocation());
        if (gate != null && !gate.isFirstStage()) {
            gate.close();
        }
    }

}
