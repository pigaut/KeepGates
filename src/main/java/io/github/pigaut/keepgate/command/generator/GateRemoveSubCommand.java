package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateRemoveSubCommand extends LangSubCommand {

    public GateRemoveSubCommand(@NotNull KeepGatePlugin plugin) {
        super("remove-gate", plugin);

        withPlayerExecution((player, args, placeholders) -> {
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders);
                return;
            }
            final Location location = targetBlock.getLocation();
            final Gate generator = plugin.getGate(location);
            if (generator == null) {
                plugin.sendMessage(player, "target-not-gate", placeholders);
                return;
            }
            plugin.getGates().removeGate(generator);
            plugin.sendMessage(player, "removed-gate", placeholders, generator);
        });

    }

}
