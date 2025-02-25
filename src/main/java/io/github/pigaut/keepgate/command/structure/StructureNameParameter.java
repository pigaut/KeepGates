package io.github.pigaut.keepgate.command.structure;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class StructureNameParameter extends CommandParameter {

    public StructureNameParameter(@NotNull KeepGatePlugin plugin) {
        super(plugin.getLang("structure-name-parameter"),
                (sender, args) -> plugin.getStructures().getStructureNames());
    }

}
