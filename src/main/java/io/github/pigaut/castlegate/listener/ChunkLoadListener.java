package io.github.pigaut.castlegate.listener;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.voxel.*;
import io.github.pigaut.voxel.hologram.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.*;

public class ChunkLoadListener implements Listener {

    private final GatePlugin plugin;

    public ChunkLoadListener(GatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        for (Gate gate : plugin.getGates().getAllGates()) {
            if (SpigotLibs.areChunksEqual(chunk, gate.getOrigin().getChunk())) {
                final HologramDisplay hologram = gate.getCurrentHologram();
                if (hologram != null && !hologram.exists()) {
                    hologram.spawn();
                }
            }
        }
    }

}
