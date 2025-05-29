package io.github.pigaut.castlegate.event;

import io.github.pigaut.castlegate.gate.*;
import io.github.pigaut.castlegate.player.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GateInteractEvent extends PlayerGateEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GateInteractEvent(GatePlayer player, Gate gate) {
        super(player, gate);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
