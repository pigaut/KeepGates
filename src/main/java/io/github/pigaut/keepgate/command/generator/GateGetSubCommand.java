package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GateGetSubCommand extends LangSubCommand {

    public GateGetSubCommand(@NotNull KeepGatePlugin plugin) {
        super("get-gate", plugin);
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GateTemplate generator = plugin.getGateTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            plugin.sendMessage(player, "received-gate", placeholders, generator);
        });
    }

}
