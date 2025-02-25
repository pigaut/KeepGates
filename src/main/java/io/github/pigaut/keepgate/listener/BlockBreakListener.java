package io.github.pigaut.keepgate.listener;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockBreakListener implements Listener {

    private final KeepGatePlugin plugin;

    public BlockBreakListener(KeepGatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Gate gate = plugin.getGate(block.getLocation());
        if (gate == null) {
            return;
        }

        event.setCancelled(true);
        if (!gate.matchBlocks()) {
            plugin.getGates().removeGate(gate);
        }
    }

}
