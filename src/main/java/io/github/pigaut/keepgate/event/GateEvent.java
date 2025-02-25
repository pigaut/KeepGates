package io.github.pigaut.keepgate.event;

import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.voxel.event.*;

public abstract class GateEvent extends CancellableEvent {

    private final Gate generator;

    protected GateEvent(Gate generator) {
        this.generator = generator;
    }

    public Gate getGenerator() {
        return generator;
    }

}
