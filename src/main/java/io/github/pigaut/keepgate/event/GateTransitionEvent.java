package io.github.pigaut.keepgate.event;

import io.github.pigaut.keepgate.gate.*;
import org.bukkit.event.*;

public class GateTransitionEvent extends GateEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GateTransitionEvent(Gate generator) {
        super(generator);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
