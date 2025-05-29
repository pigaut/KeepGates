package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateRemoveSubCommand extends SubCommand {

    public GateRemoveSubCommand(@NotNull GatePlugin plugin) {
        super("remove", plugin);
        withPermission(plugin.getPermission("gate.remove"));
        withDescription(plugin.getLang("gate-remove-command"));
        withPlayerExecution((player, args, placeholders) -> {
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders);
                return;
            }
            final Location location = targetBlock.getLocation();
            final Gate gate = plugin.getGate(location);
            if (gate == null) {
                plugin.sendMessage(player, "target-not-gate", placeholders);
                return;
            }
            plugin.getGates().unregisterGate(gate);
            plugin.sendMessage(player, "removed-gate", placeholders, gate);
        });

    }

}
