package io.github.pigaut.castlegate.event;

import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.voxel.event.*;

public abstract class GateEvent extends CancellableEvent {

    private final Gate gate;

    protected GateEvent(Gate gate) {
        this.gate = gate;
    }

    public Gate getGate() {
        return gate;
    }

}
