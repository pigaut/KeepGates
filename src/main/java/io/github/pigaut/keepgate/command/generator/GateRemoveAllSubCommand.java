package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.player.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateRemoveAllSubCommand extends LangSubCommand {

    public GateRemoveAllSubCommand(@NotNull KeepGatePlugin plugin) {
        super("remove-all-gates", plugin);
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final KeepGatePlayer playerState = plugin.getPlayer(player.getUniqueId());
            if (playerState == null) {
                plugin.sendMessage(player, "loading-player-data", placeholders);
                return;
            }
            final GateTemplate generator = plugin.getGateTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }
            final Location firstSelection = playerState.getFirstSelection();
            final Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, "incomplete-region", placeholders);
                return;
            }
            for (Location point : GateTools.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                final Gate blockGenerator = plugin.getGate(point);
                if (blockGenerator == null) {
                    continue;
                }
                if (blockGenerator.getTemplate() == generator) {
                    plugin.getGates().removeGate(blockGenerator);
                }
            }
            plugin.sendMessage(player, "removed-all-gates", placeholders, generator);
        });
    }

}
