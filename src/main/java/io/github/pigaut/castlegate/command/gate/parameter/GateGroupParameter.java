package io.github.pigaut.castlegate.command.gate.parameter;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GateGroupParameter extends CommandParameter {

    public GateGroupParameter(@NotNull GatePlugin plugin) {
        super("gate-group", (sender, args) -> plugin.getGateTemplates().getAllGroups());
    }

}
