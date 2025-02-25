package io.github.pigaut.keepgate.listener;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;

public class CropChangeListener implements Listener {

    private final KeepGatePlugin plugin;

    public CropChangeListener(KeepGatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWaterFlow(BlockFromToEvent event) {
        final Gate generator = plugin.getGate(event.getToBlock().getLocation());
        if (generator != null) {
            plugin.getGates().removeGate(generator);
            final Location location = generator.getOrigin();
            plugin.getLogger().warning("Removed generator at " + location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ". " +
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
            final Gate generator = plugin.getGate(cropLocation);
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTrample(EntityInteractEvent event) {
        final Block block = event.getBlock();
        if (block.getType() == Material.FARMLAND) {
            final Location cropLocation = block.getLocation().add(0, 1, 0);
            final Gate generator = plugin.getGate(cropLocation);
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

}
