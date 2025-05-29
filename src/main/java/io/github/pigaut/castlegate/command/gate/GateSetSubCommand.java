package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.parameter.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateSetSubCommand extends SubCommand {

    public GateSetSubCommand(@NotNull GatePlugin plugin) {
        super("set", plugin);
        withPermission(plugin.getPermission("gate.set"));
        withDescription(plugin.getLang("gate-set-command"));
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }

            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders, gate);
                return;
            }
            final Location location = targetBlock.getLocation();
            try {
                Gate.create(gate, location);
            } catch (GateOverlapException e) {
                plugin.sendMessage(player, "gate-overlap");
                return;
            }
            plugin.sendMessage(player, "created-gate", placeholders, gate);
        });
    }

}
