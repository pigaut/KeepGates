package io.github.pigaut.keepgate.util;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.gate.template.*;
import org.bukkit.*;

public class KeepGateAPI {

    private static final KeepGatePlugin plugin = KeepGatePlugin.getPlugin();

    public static GateTemplate getGateTemplate(String name) {
        return plugin.getGateTemplate(name);
    }

    public static boolean isGate(Location location) {
        return plugin.getGates().isGate(location);
    }

}
