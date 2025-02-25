package io.github.pigaut.keepgate.listener;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.event.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.player.*;
import io.github.pigaut.keepgate.stage.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class PlayerInteractListener implements Listener {

    private final KeepGatePlugin plugin;

    public PlayerInteractListener(KeepGatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWandClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || !event.hasItem()
                || event.getHand() != EquipmentSlot.HAND
                || !GateTools.isWandTool(event.getItem())) {
            return;
        }
        event.setCancelled(true);

        final Player player = event.getPlayer();
        final KeepGatePlayer playerState = plugin.getPlayer(player.getUniqueId());

        if (!player.hasPermission(plugin.getLang("wand-permission"))) {
            plugin.sendMessage(player, "missing-wand-permission");
            return;
        }

        final Location targetLocation = event.getClickedBlock().getLocation();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            playerState.setFirstSelection(targetLocation);
            plugin.sendMessage(player, "selected-first-position");
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            playerState.setSecondSelection(targetLocation);
            plugin.sendMessage(player, "selected-second-position");
        }
    }

    @EventHandler
    public void onGeneratorItemClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack heldItem = event.getItem();
        if (heldItem == null) {
            return;
        }

        final GateTemplate heldGenerator = GateTools.getGeneratorFromTool(heldItem);
        if (heldGenerator == null) {
            return;
        }

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            event.setCancelled(true);
            if (!player.hasPermission(plugin.getLang("gate-rotate-permission"))) {
                plugin.sendMessage(player, "missing-rotate-permission", heldGenerator);
                return;
            }
            GateTools.incrementToolRotation(heldItem);
            PlayerUtil.sendActionBar(player, plugin.getLang("changed-gate-rotation"));
            return;
        }

        final Block clickedBlock = event.getClickedBlock();

        if (action == Action.LEFT_CLICK_BLOCK) {
            final Gate clickedGenerator = plugin.getGate(clickedBlock.getLocation());
            if (clickedGenerator == null || !clickedGenerator.getTemplate().equals(heldGenerator)) {
                return;
            }

            event.setCancelled(true);
            if (!player.hasPermission(plugin.getLang("gate-break-permission"))) {
                plugin.sendMessage(player, "missing-break-permission", heldGenerator);
                return;
            }
            plugin.getGates().removeGate(clickedGenerator);
            PlayerUtil.sendActionBar(player, plugin.getLang("broke-gate"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorPlace(BlockPlaceEvent event) {
        final ItemStack heldItem = event.getItemInHand();
        final GateTemplate heldGenerator = GateTools.getGeneratorFromTool(heldItem);
        if (heldGenerator == null) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getLang("gate-place-permission"))) {
            plugin.sendMessage(player, "missing-place-permission", heldGenerator);
            return;
        }

        final Block blockPlaced = event.getBlockPlaced();
        final Location targetLocation = blockPlaced.getLocation();

        plugin.getScheduler().runTaskLater(1, () -> {
            blockPlaced.setType(Material.AIR);
            try {
                Gate.create(heldGenerator, targetLocation, GateTools.getToolRotation(heldItem));
            } catch (GateOverlapException e) {
                final ItemStack placedItem = heldItem.clone();
                placedItem.setAmount(1);
                PlayerUtil.giveItemsOrDrop(player, placedItem);
                PlayerUtil.sendActionBar(player, plugin.getLang("gate-overlap"));
                return;
            }
            PlayerUtil.sendActionBar(player, plugin.getLang("placed-gate"));
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Gate clickedGenerator = plugin.getGate(event.getClickedBlock().getLocation());
        if (clickedGenerator == null) {
            return;
        }

        if (!clickedGenerator.matchBlocks()) {
            plugin.getGates().removeGate(clickedGenerator);
            return;
        }

        final KeepGatePlayer playerState = plugin.getPlayer(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(clickedGenerator);

        final GeneratorInteractEvent generatorInteractEvent = new GeneratorInteractEvent(playerState, clickedGenerator);
        SpigotServer.callEvent(generatorInteractEvent);
        if (generatorInteractEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        final GateStage stage = clickedGenerator.getCurrentStage();
        final BlockClickFunction clickFunction = stage.getClickFunction();
        if (clickFunction != null) {
            clickFunction.onBlockClick(playerState, event);
        }
    }

}
