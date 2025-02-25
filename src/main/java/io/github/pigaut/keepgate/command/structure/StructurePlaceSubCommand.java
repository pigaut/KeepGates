package io.github.pigaut.keepgate.command.structure;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.structure.*;
import org.bukkit.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class StructurePlaceSubCommand extends LangSubCommand {

    public StructurePlaceSubCommand(@NotNull KeepGatePlugin plugin) {
        super("place-structure", plugin);
        addParameter(new StructureNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final BlockStructure structure = plugin.getBlockStructure(args[0]);
            if (structure == null) {
                plugin.sendMessage(player, "structure-not-found", placeholders);
                return;
            }
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders);
                return;
            }
            final Location location = targetBlock.getLocation();
            structure.updateBlocks(location, Rotation.NONE);
            plugin.sendMessage(player, "placed-structure", placeholders);
        });
    }

}
