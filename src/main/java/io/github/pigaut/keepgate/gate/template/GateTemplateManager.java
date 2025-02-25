package io.github.pigaut.keepgate.gate.template;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GateTemplateManager extends Manager {

    private final KeepGatePlugin plugin;
    private final Map<String, GateTemplate> generatorsByName = new ConcurrentHashMap<>();

    public GateTemplateManager(KeepGatePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public List<String> getGateNames() {
        return new ArrayList<>(generatorsByName.keySet());
    }

    public Collection<GateTemplate> getAllGateTemplates() {
        return new ArrayList<>(generatorsByName.values());
    }

    public @Nullable GateTemplate getGateTemplate(String name) {
        return name != null ? generatorsByName.get(name) : null;
    }

    public void registerGate(@NotNull String name, @NotNull GateTemplate generator) {
        generatorsByName.put(name, generator);
    }

    @Override
    public void loadData() {
        generatorsByName.clear();
        for (File generatorFile : plugin.getFiles("gates")) {
            final RootSequence config = plugin.loadConfigSequence(generatorFile);
            generatorsByName.put(config.getName(), config.load(GateTemplate.class));
        }
    }

}
