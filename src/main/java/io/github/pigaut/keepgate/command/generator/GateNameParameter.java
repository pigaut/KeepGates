package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GateNameParameter extends CommandParameter {

    public GateNameParameter(@NotNull KeepGatePlugin plugin) {
        super(plugin.getLang("gate-name-parameter"),
                (sender, args) -> plugin.getGateTemplates().getGateNames());
    }

}
