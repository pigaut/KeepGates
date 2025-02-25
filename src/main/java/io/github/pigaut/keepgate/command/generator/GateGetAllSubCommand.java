package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GateGetAllSubCommand extends LangSubCommand {

    public GateGetAllSubCommand(@NotNull KeepGatePlugin plugin) {
        super("get-all-gates", plugin);
        withPlayerExecution((player, args, placeholders) -> {
            for (GateTemplate generator : plugin.getGateTemplates().getAllGateTemplates()) {
                PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            }
            plugin.sendMessage(player, "received-all-gates", placeholders);
        });
    }
}
