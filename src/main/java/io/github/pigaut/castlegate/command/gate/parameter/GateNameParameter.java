package io.github.pigaut.castlegate.command.gate.parameter;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GateNameParameter extends CommandParameter {

    public GateNameParameter(@NotNull GatePlugin plugin) {
        super("gate-name", (sender, args) -> plugin.getGateTemplates().getAllNames());
    }

}
