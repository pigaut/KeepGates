package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateSetSubCommand extends LangSubCommand {

    public GateSetSubCommand(@NotNull KeepGatePlugin plugin) {
        super("set-gate", plugin);
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GateTemplate generator = plugin.getGateTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders, generator);
                return;
            }
            final Location location = targetBlock.getLocation();
            try {
                Gate.create(generator, location);
            } catch (GateOverlapException e) {
                plugin.sendMessage(player, "gate-overlap");
                return;
            }
            plugin.sendMessage(player, "created-gate", placeholders, generator);
        });
    }

}
