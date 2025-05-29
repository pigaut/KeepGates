package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.parameter.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateGetGroupSubCommand extends SubCommand {

    public GateGetGroupSubCommand(@NotNull GatePlugin plugin) {
        super("get-group", plugin);
        withPermission(plugin.getPermission("gate.get-group"));
        withDescription(plugin.getLang("gate-get-group-command"));
        addParameter(new GateGroupParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final List<GateTemplate> groupGates = plugin.getGateTemplates().getAll(args[0]);

            if (groupGates.isEmpty()) {
                plugin.sendMessage(player, "gate-group-not-found", placeholders);
                return;
            }

            for (GateTemplate gate : groupGates) {
                PlayerUtil.giveItemsOrDrop(player, gate.getItem());
            }
            plugin.sendMessage(player, "received-gate-group", placeholders);
        });
    }

}
