package io.github.pigaut.keepgate.command.structure;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.player.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.command.parameter.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.*;
import org.snakeyaml.engine.v2.common.*;

import java.io.*;

public class StructureSaveSubCommand extends LangSubCommand {

    public StructureSaveSubCommand(@NotNull KeepGatePlugin plugin) {
        super("save-structure", plugin);
        addParameter(new FilePathParameter(plugin, "structures"));
        withPlayerExecution((player, args, placeholders) -> {
            final KeepGatePlayer playerState = plugin.getPlayer(player.getUniqueId());
            if (playerState == null) {
                plugin.sendMessage(player, "loading-player-data", placeholders);
                return;
            }

            final File file = plugin.getFile("structures", args[0]);
            YamlConfig.createFileIfNotExists(file);

            if (!YamlConfig.isYamlFile(file)) {
                plugin.sendMessage(player, "not-yaml-file", placeholders);
                return;
            }

            final Location firstSelection = playerState.getFirstSelection();
            final Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, "incomplete-region", placeholders);
                return;
            }

            final int centerX = (int) ((firstSelection.getBlockX() + secondSelection.getBlockX()) / 2.0);
            final int lowestY = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY());
            final int centerZ = (int) ((firstSelection.getBlockZ() + secondSelection.getBlockZ()) / 2.0);

            final RootSequence config = plugin.loadConfigSequence(file);
            config.setFlowStyle(FlowStyle.AUTO);
            config.clear();
            for (Location location : GateTools.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                final Block block = location.getBlock();
                final Material blockType = block.getType();
                if (plugin.getStructures().isBlacklisted(blockType)) {
                    continue;
                }

                final ConfigSection blockConfig = config.addSection();
                blockConfig.set("block", blockType);

                final BlockData blockData = block.getBlockData();

                if (blockData instanceof Ageable ageable) {
                    blockConfig.set("age", ageable.getAge());
                }

                if (blockData instanceof Directional directional) {
                    blockConfig.set("direction|face", directional.getFacing());
                } else if (blockData instanceof Rotatable rotatable) {
                    blockConfig.set("direction|face", rotatable.getRotation());
                }

                if (blockData instanceof MultipleFacing multipleFacing) {
                    blockConfig.set("directions|faces", multipleFacing.getFaces());
                }

                if (blockData instanceof Orientable orientable) {
                    blockConfig.set("orientation", orientable.getAxis());
                }

                if (blockData instanceof Openable openable) {
                    blockConfig.set("open", openable.isOpen());
                }

                if (blockData instanceof Bisected bisected) {
                    blockConfig.set("half", bisected.getHalf());
                }

                if (blockData instanceof Stairs stairs) {
                    blockConfig.set("stair-shape|stairs-shape|stairs", stairs.getShape());
                }

                if (blockData instanceof Slab slab) {
                    blockConfig.set("slab-type|slab", slab.getType());
                }

                if (blockData instanceof Door door) {
                    blockConfig.set("door-hinge|door", door.getHinge());
                }

                if (blockData instanceof Bed bed) {
                    blockConfig.set("bed-part|bed", bed.getPart());
                }

                blockConfig.set("offset.x", location.getBlockX() - centerX);
                blockConfig.set("offset.y", location.getBlockY() - lowestY);
                blockConfig.set("offset.z", location.getBlockZ() - centerZ);
            }

            if (config.size() < 2) {
                plugin.sendMessage(player, "structure-minimum-blocks", placeholders);
                return;
            }

            config.save();
            plugin.getStructures().reload();
            plugin.sendMessage(player, "saved-structure", placeholders);
        });
    }

}
