package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.parameter.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GateGetSubCommand extends SubCommand {

    public GateGetSubCommand(@NotNull GatePlugin plugin) {
        super("get", plugin);
        withPermission(plugin.getPermission("gate.get"));
        withDescription(plugin.getLang("gate-get-command"));
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, gate.getItem());
            plugin.sendMessage(player, "received-gate", placeholders, gate);
        });
    }

}
