package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorLoader implements ConfigLoader<GeneratorTemplate> {

    @Override
    public @NotNull String getProblemDescription() {
        return "Could not load generator";
    }

    @Override
    public @NotNull GeneratorTemplate loadFromSequence(@NotNull ConfigSequence config) throws InvalidConfigurationException {
        final String name = config.getRoot().getName();
        final List<GeneratorStage> generatorStages = new ArrayList<>();
        final GeneratorTemplate generator = new GeneratorTemplate(name, generatorStages);
        for (ConfigSection nestedSection : config.getNestedSections()) {
            generatorStages.add(loadStage(generator, nestedSection));
        }

        if (generatorStages.size() < 2) {
            throw new InvalidConfigurationException(config, "Generator must have one depleted and one replenished stage");
        }

        if (generatorStages.get(0).getState() != GeneratorState.DEPLETED) {
            throw new InvalidConfigurationException(config, "The first stage must be depleted");
        }

        final GeneratorStage lastStage = generatorStages.get(generatorStages.size() - 1);
        if (lastStage.getState() != GeneratorState.REPLENISHED) {
            throw new InvalidConfigurationException(config, "The last stage must be replenished");
        }

        if (lastStage.getStructure() instanceof SingleBlockStructure singleBlockStructure) {
            generator.setItemType(singleBlockStructure.getType());
        }

        for (int i = 1; i < generatorStages.size(); i++) {
            if (generatorStages.get(i).getState() == GeneratorState.DEPLETED) {
                throw new InvalidConfigurationException(config, "Only the first stage is depleted");
            }
        }
        return generator;
    }

    private GeneratorStage loadStage(GeneratorTemplate generator, ConfigSection config) {
        final GeneratorState state = config.get("type|state", GeneratorState.class);
        BlockStructure structure = config.getOptional("structure", BlockStructure.class).orElse(null);
        if (structure == null) {
            structure = config.load(BlockStructure.class);
        }
        final boolean dropItems = config.getOptionalBoolean("drop-items").orElse(true);
        final Integer expToDrop = config.getOptionalInteger("exp-to-drop").orElse(null);
        final int growthTime = config.getOptionalInteger("growth|growth-time").orElse(0);
        final Double chance = config.getOptionalDouble("chance|growth-chance").orElse(null);
        final Function onBreak = config.getOptional("on-break", Function.class).orElse(null);
        final Function onGrowth = config.getOptional("on-growth", Function.class).orElse(null);
        final BlockClickFunction onClick = config.getOptional("on-click", BlockClickFunction.class).orElse(null);
        final Hologram hologram = config.getOptional("hologram", Hologram.class).orElse(null);

        if (structure instanceof OffsetBlockStructure) {
            throw new InvalidConfigurationException(config, "block", "A single block generator cannot have an offset");
        }

        if (growthTime < 0) {
            throw new InvalidConfigurationException(config, "growth", "The growth timer must be a positive number");
        }

        return new GeneratorStage(generator, state, structure, dropItems, expToDrop, growthTime,
                chance, onBreak, onGrowth, onClick, hologram);
    }

}