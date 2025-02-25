package io.github.pigaut.keepgate.config;

import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.stage.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorLoader implements ConfigLoader<GateTemplate> {

    @Override
    public @NotNull String getProblemDescription() {
        return "invalid generator";
    }

    @Override
    public @NotNull GateTemplate loadFromSequence(@NotNull ConfigSequence config) throws InvalidConfigurationException {
        final String name = config.getRoot().getName();
        final List<GateStage> generatorStages = new ArrayList<>();
        final GateTemplate generator = new GateTemplate(name, generatorStages);
        for (ConfigSection nestedSection : config.getNestedSections()) {
            generatorStages.add(loadStage(generator, nestedSection));
        }

        if (generatorStages.size() < 2) {
            throw new InvalidConfigurationException(config, "Gate must have at least one closed and one opened stage");
        }

        return generator;
    }

    private GateStage loadStage(GateTemplate generator, ConfigSection config) {
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