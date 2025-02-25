package io.github.pigaut.keepgate.player;

import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerManager extends PlayerManager<KeepGatePlayer> {

    public OrestackPlayerManager(@NotNull EnhancedPlugin plugin) {
        super(plugin, KeepGatePlayer::new);
    }

}
