package io.github.pigaut.keepgate.event;

import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.player.*;

public abstract class PlayerGateEvent extends GateEvent {

    private final KeepGatePlayer player;

    protected PlayerGateEvent(KeepGatePlayer player, Gate generator) {
        super(generator);
        this.player = player;
    }

    public KeepGatePlayer getPlayer() {
        return player;
    }

}
