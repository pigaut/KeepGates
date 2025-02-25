package io.github.pigaut.keepgate.listener;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.*;
import io.github.pigaut.voxel.hologram.display.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.*;

public class ChunkLoadListener implements Listener {

    private final KeepGatePlugin plugin;

    public ChunkLoadListener(KeepGatePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        for (Gate generator : plugin.getGates().getAllGenerators()) {
            if (SpigotLibs.areChunksEqual(chunk, generator.getOrigin().getChunk())) {
                final HologramDisplay hologram = generator.getCurrentHologram();
                if (hologram != null && !hologram.exists()) {
                    hologram.spawn();
                }
            }
        }
    }

}
