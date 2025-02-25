package io.github.pigaut.keepgate.command.structure;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class StructureSubCommand extends LangSubCommand {

    public StructureSubCommand(@NotNull KeepGatePlugin plugin) {
        super("structure", plugin);
        addSubCommand(new StructureSaveSubCommand(plugin));
        addSubCommand(new StructurePlaceSubCommand(plugin));
    }

}
