package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.parameter.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.player.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.core.structure.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateRemoveAllSubCommand extends SubCommand {

    public GateRemoveAllSubCommand(@NotNull GatePlugin plugin) {
        super("remove-all", plugin);
        withPermission(plugin.getPermission("gate.remove-all"));
        withDescription(plugin.getLang("gate-remove-all-command"));
        addParameter(new GateNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GatePlayer playerState = plugin.getPlayerState(player.getUniqueId());
            if (playerState == null) {
                plugin.sendMessage(player, "loading-player-data", placeholders);
                return;
            }
            final GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, "gate-not-found", placeholders);
                return;
            }
            final Location firstSelection = playerState.getFirstSelection();
            final Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, "incomplete-region", placeholders);
                return;
            }
            for (Location point : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                final Gate blockGate = plugin.getGate(point);
                if (blockGate == null) {
                    continue;
                }
                if (blockGate.getTemplate() == gate) {
                    plugin.getGates().unregisterGate(blockGate);
                }
            }
            plugin.sendMessage(player, "removed-all-gates", placeholders, gate);
        });
    }

}
