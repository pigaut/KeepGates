package io.github.pigaut.keepgate.command.generator;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.player.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateSetAllSubCommand extends LangSubCommand {

    public GateSetAllSubCommand(@NotNull KeepGatePlugin plugin) {
        super("set-all-gates", plugin);
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
                plugin.sendMessage(player, "incomplete-region", placeholders, generator);
                return;
            }
            final BlockStructure structure = generator.getLastStage().getStructure();
            for (Location location : GateTools.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                for (Rotation rotation : Rotation.values()) {
                    if (structure.matchBlocks(location, rotation)) {
                        try {
                            Gate.create(generator, location);
                        } catch (GateOverlapException ignored) {
                        }
                    }
                }
            }
            plugin.sendMessage(player, "created-all-gates", placeholders, generator);
        });
    }

}
