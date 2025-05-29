package io.github.pigaut.castlegate.player;

import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class GatePlayerManager extends PlayerStateManager<GatePlayer> {

    public GatePlayerManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, GatePlayer::new);
    }

}
