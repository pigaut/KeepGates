package io.github.pigaut.castlegate.gate.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;

public class GateTemplateManager extends ManagerContainer<GateTemplate> {

    public GateTemplateManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @Nullable String getFilesDirectory() {
        return "gates";
    }

    @Override
    public void loadFile(@NotNull File file) {
        final RootSequence config = new RootSequence(file, plugin.getConfigurator());
        config.setPrefix("Gates");
        config.load();
        final GateTemplate gate = config.load(GateTemplate.class);
        this.add(gate);
    }

}
