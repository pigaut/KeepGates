package io.github.pigaut.keepgate.event;

import io.github.pigaut.keepgate.gate.*;
import io.github.pigaut.keepgate.player.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorInteractEvent extends PlayerGateEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorInteractEvent(KeepGatePlayer player, Gate generator) {
        super(player, generator);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
