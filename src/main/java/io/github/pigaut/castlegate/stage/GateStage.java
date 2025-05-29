package io.github.pigaut.castlegate.stage;

import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import org.jetbrains.annotations.*;

public class GateStage {

    private final BlockStructure structure;
    private final int delay;
    private final @Nullable Function onTransition;
    private final @Nullable BlockClickFunction onClick;
    private final @Nullable Hologram hologram;

    public GateStage(@NotNull BlockStructure structure, int delay, @Nullable Function onTransition,
                     @Nullable BlockClickFunction onClick, @Nullable Hologram hologram) {
        this.structure = structure;
        this.delay = delay;
        this.onTransition = onTransition;
        this.onClick = onClick;
        this.hologram = hologram;
    }

    public int getDelay() {
        return delay;
    }

    public @NotNull BlockStructure getStructure() {
        return structure;
    }

    public @Nullable Function getTransitionFunction() {
        return onTransition;
    }

    public @Nullable BlockClickFunction getClickFunction() {
        return onClick;
    }

    public @Nullable Hologram getHologram() {
        return hologram;
    }

    @Override
    public String toString() {
        return "GateStage{" +
                "structure=" + structure +
                ", delay=" + delay +
                ", onGrowth=" + onTransition +
                ", onClick=" + onClick +
                ", hologram=" + hologram +
                '}';
    }

}
