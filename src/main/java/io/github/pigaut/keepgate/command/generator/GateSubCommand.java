package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class GateSubCommand extends LangSubCommand {

    public GateSubCommand(@NotNull KeepGatePlugin plugin) {
        super("gate", plugin);
        addSubCommand(new GateGetSubCommand(plugin));
        addSubCommand(new GateGetAllSubCommand(plugin));
        addSubCommand(new GateSetSubCommand(plugin));
        addSubCommand(new GateRemoveSubCommand(plugin));
        addSubCommand(new GateSetAllSubCommand(plugin));
        addSubCommand(new GateRemoveAllSubCommand(plugin));
    }

}
