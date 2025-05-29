package io.github.pigaut.castlegate.command.gate;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.parameter.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.player.*;
import io.github.pigaut.castlegate.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateSetAllSubCommand extends SubCommand {

    public GateSetAllSubCommand(@NotNull GatePlugin plugin) {
        super("set-all", plugin);
        withPermission(plugin.getPermission("gate.set-all"));
        withDescription(plugin.getLang("gate-set-all-command"));
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
                plugin.sendMessage(player, "incomplete-region", placeholders, gate);
                return;
            }
            final BlockStructure structure = gate.getLastStage().getStructure();
            for (Location location : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                for (Rotation rotation : Rotation.values()) {
                    if (structure.matchBlocks(location, rotation)) {
                        try {
                            Gate.create(gate, location);
                        } catch (GateOverlapException ignored) {
                        }
                    }
                }
            }
            plugin.sendMessage(player, "created-all-gates", placeholders, gate);
        });
    }

}
