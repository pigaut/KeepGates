package io.github.pigaut.castlegate.util;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.template.*;
import org.bukkit.*;

public class KeepGateAPI {

    private static final GatePlugin plugin = GatePlugin.getPlugin();

    public static GateTemplate getGateTemplate(String name) {
        return plugin.getGateTemplate(name);
    }

    public static boolean isGate(Location location) {
        return plugin.getGates().isGate(location);
    }

}
