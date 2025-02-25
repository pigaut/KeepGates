package io.github.pigaut.keepgate.listener;

import io.github.pigaut.keepgate.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class BlockDestructionListener implements Listener {

    private final KeepGatePlugin plugin;

    public BlockDestructionListener(KeepGatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (plugin.getGates().isGate(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (plugin.getGates().isGate(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            if (plugin.getGates().isGate(explodedBlock.getLocation())) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            if (plugin.getGates().isGate(explodedBlock.getLocation())) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            if (plugin.getGates().isGate(movedBlock.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            if (plugin.getGates().isGate(movedBlock.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

}
