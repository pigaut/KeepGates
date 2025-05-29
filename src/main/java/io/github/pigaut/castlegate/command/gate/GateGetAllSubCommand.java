package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GateGetAllSubCommand extends SubCommand {

    public GateGetAllSubCommand(@NotNull GatePlugin plugin) {
        super("get-all", plugin);
        withPermission(plugin.getPermission("gate.get-all"));
        withDescription(plugin.getLang("gate-get-all-command"));
        withPlayerExecution((player, args, placeholders) -> {
            for (GateTemplate gate : plugin.getGateTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, gate.getItem());
            }
            plugin.sendMessage(player, "received-all-gates", placeholders);
        });
    }
}
