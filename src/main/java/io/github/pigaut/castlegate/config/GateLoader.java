package io.github.pigaut.castlegate.config;

import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.castlegate.stage.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateLoader implements ConfigLoader<GateTemplate> {

    @Override
    public @NotNull String getProblemDescription() {
        return "invalid gate";
    }

    @Override
    public @NotNull GateTemplate loadFromSequence(@NotNull ConfigSequence config) throws InvalidConfigurationException {
        if (!(config instanceof ConfigRoot root)) {
            throw new InvalidConfigurationException(config, "Gate template can only be loaded from a root configuration section");
        }

        final String name = root.getName();
        final String group = FolderGroup.byFile(root.getFile(), "gates", true);
        final List<GateStage> gateStages = new ArrayList<>();
        final GateTemplate gate = new GateTemplate(name, group, gateStages);
        for (ConfigSection nestedSection : config.getNestedSections()) {
            gateStages.add(loadStage(gate, nestedSection));
        }

        if (gateStages.size() < 2) {
            throw new InvalidConfigurationException(config, "Gate must have at least one closed and one opened stage");
        }

        return gate;
    }

    private GateStage loadStage(GateTemplate template, ConfigSection config) {
        BlockStructure structure = config.getOptional("structure|structures", BlockStructure.class).orElse(null);
        if (structure == null) {
            structure = config.load(BlockStructure.class);
        }
        final int delay = config.getOptionalInteger("delay|transition-delay").orElse(0);
        final Function onTransition = config.getOptional("on-transition", Function.class).orElse(null);
        final BlockClickFunction onClick = config.getOptional("on-click", BlockClickFunction.class).orElse(null);
        final Hologram hologram = config.getOptional("hologram", Hologram.class).orElse(null);

        if (delay < 0) {
            throw new InvalidConfigurationException(config, "delay", "The transition delay must be a positive number");
        }

        return new GateStage(structure, delay, onTransition, onClick, hologram);
    }

}