package io.github.pigaut.castlegate.listener;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockBreakListener implements Listener {

    private final GatePlugin plugin;

    public BlockBreakListener(GatePlugin plugin) {
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
            plugin.getGates().unregisterGate(gate);
        }
    }

}
