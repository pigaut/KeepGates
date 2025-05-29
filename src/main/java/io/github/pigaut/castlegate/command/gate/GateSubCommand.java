package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class GateSubCommand extends SubCommand {

    public GateSubCommand(@NotNull GatePlugin plugin) {
        super("gate", plugin);
        withPermission(plugin.getPermission("gate"));
        withDescription(plugin.getLang("gate-command"));
        addSubCommand(new GateGetSubCommand(plugin));
        addSubCommand(new GateGetGroupSubCommand(plugin));
        addSubCommand(new GateGetAllSubCommand(plugin));
        addSubCommand(new GateSetSubCommand(plugin));
        addSubCommand(new GateRemoveSubCommand(plugin));
        addSubCommand(new GateSetAllSubCommand(plugin));
        addSubCommand(new GateRemoveAllSubCommand(plugin));
    }

}
