package io.github.pigaut.castlegate.listener;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.voxel.bukkit.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;

public class CropChangeListener implements Listener {

    private final GatePlugin plugin;

    public CropChangeListener(GatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWaterFlow(BlockFromToEvent event) {
        final Gate gate = plugin.getGate(event.getToBlock().getLocation());
        if (gate != null) {
            plugin.getGates().unregisterGate(gate);
            final Location location = gate.getOrigin();
            plugin.getLogger().warning("Removed gate at " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ". " +
                    "Reason: water/lava destroyed the block.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCropPhysics(BlockPhysicsEvent event) {
        final Block block = event.getBlock();
        if (Crops.isCrop(block.getType())) {
            if (plugin.getGates().isGate(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onStructureGrowth(StructureGrowEvent event) {
        if (plugin.getGates().isGate(event.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        if (plugin.getGates().isGate(event.getSource().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onForm(BlockFormEvent event) {
        if (plugin.getGates().isGate(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrowth(BlockGrowEvent event) {
        if (plugin.getGates().isGate(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrample(PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (event.getAction() == Action.PHYSICAL && block.getType() == Material.FARMLAND) {
            final Location cropLocation = block.getLocation().add(0, 1, 0);
            final Gate gate = plugin.getGate(cropLocation);
            if (gate != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrample(EntityInteractEvent event) {
        final Block block = event.getBlock();
        if (block.getType() == Material.FARMLAND) {
            final Location cropLocation = block.getLocation().add(0, 1, 0);
            final Gate gate = plugin.getGate(cropLocation);
            if (gate != null) {
                event.setCancelled(true);
            }
        }
    }

}
