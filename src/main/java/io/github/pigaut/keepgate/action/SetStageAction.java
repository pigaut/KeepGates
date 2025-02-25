package io.github.pigaut.keepgate.action;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class SetStageAction implements BlockAction {

    private final KeepGatePlugin plugin = KeepGatePlugin.getPlugin();

    private final int stage;

    public SetStageAction(int stage) {
        this.stage = stage;
    }

    @Override
    public void execute(@NotNull Block block) {
        final Gate generator = plugin.getGate(block.getLocation());
        if (generator != null && stage <= generator.getTemplate().getMaxStage()) {
            generator.setCurrentStage(stage);
        }
    }

}
