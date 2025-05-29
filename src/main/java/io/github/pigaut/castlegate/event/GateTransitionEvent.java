package io.github.pigaut.castlegate.event;

import io.github.pigaut.castlegate.gate.*;
import org.bukkit.event.*;

public class GateTransitionEvent extends GateEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GateTransitionEvent(Gate gate) {
        super(gate);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
