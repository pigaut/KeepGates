package io.github.pigaut.castlegate.event;

import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.player.*;

public abstract class PlayerGateEvent extends GateEvent {

    private final GatePlayer player;

    protected PlayerGateEvent(GatePlayer player, Gate gate) {
        super(gate);
        this.player = player;
    }

    public GatePlayer getPlayer() {
        return player;
    }

}
