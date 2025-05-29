package io.github.pigaut.castlegate.listener;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.event.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.player.*;
import io.github.pigaut.castlegate.stage.*;
import io.github.pigaut.castlegate.util.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
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

    private final GatePlugin plugin;

    public PlayerInteractListener(GatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGateItemClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack heldItem = event.getItem();
        if (heldItem == null) {
            return;
        }

        final GateTemplate heldGate = GateTools.getGateFromTool(heldItem);
        if (heldGate == null) {
            return;
        }

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            event.setCancelled(true);
            if (!player.hasPermission("castlegate.gate.rotate")) {
                plugin.sendMessage(player, "missing-rotate-permission", heldGate);
                return;
            }
            GateTools.incrementToolRotation(heldItem);
            PlayerUtil.sendActionBar(player, plugin.getLang("changed-gate-rotation"));
            return;
        }

        final Block clickedBlock = event.getClickedBlock();

        if (action == Action.LEFT_CLICK_BLOCK) {
            final Gate clickedGate = plugin.getGate(clickedBlock.getLocation());
            if (clickedGate == null || !clickedGate.getTemplate().equals(heldGate)) {
                return;
            }

            event.setCancelled(true);
            if (!player.hasPermission("castlegate.gate.break")) {
                plugin.sendMessage(player, "missing-break-permission", heldGate);
                return;
            }
            plugin.getGates().unregisterGate(clickedGate);
            PlayerUtil.sendActionBar(player, plugin.getLang("broke-gate"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGatePlace(BlockPlaceEvent event) {
        final ItemStack heldItem = event.getItemInHand();
        final GateTemplate heldGate = GateTools.getGateFromTool(heldItem);
        if (heldGate == null) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission("castlegate.gate.place")) {
            plugin.sendMessage(player, "missing-place-permission", heldGate);
            return;
        }

        final Block blockPlaced = event.getBlockPlaced();
        final Location targetLocation = blockPlaced.getLocation();

        plugin.getScheduler().runTaskLater(1, () -> {
            blockPlaced.setType(Material.AIR, false);
            try {
                Gate.create(heldGate, targetLocation, GateTools.getToolRotation(heldItem));
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
    public void onGateInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Gate clickedGate = plugin.getGate(event.getClickedBlock().getLocation());
        if (clickedGate == null) {
            return;
        }

        event.setCancelled(true);

        if (!clickedGate.matchBlocks()) {
            plugin.getGates().unregisterGate(clickedGate);
            return;
        }

        final GatePlayer playerState = plugin.getPlayerState(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(clickedGate);

        final GateInteractEvent gateInteractEvent = new GateInteractEvent(playerState, clickedGate);
        SpigotServer.callEvent(gateInteractEvent);
        if (gateInteractEvent.isCancelled()) {
            return;
        }

        final GateStage stage = clickedGate.getCurrentStage();
        final BlockClickFunction clickFunction = stage.getClickFunction();
        if (clickFunction != null) {
            clickFunction.onBlockClick(playerState, event);
        }
    }

}
