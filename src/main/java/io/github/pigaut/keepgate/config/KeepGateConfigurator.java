package io.github.pigaut.keepgate.config;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.action.*;
import io.github.pigaut.keepgate.gate.template.*;
import io.github.pigaut.keepgate.structure.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.function.action.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.structure.config.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

public class KeepGateConfigurator extends PluginConfigurator {

    public KeepGateConfigurator(@NotNull KeepGatePlugin plugin) {
        super(plugin);
        addLoader(GateTemplate.class, new GeneratorLoader());
        addLoader(BlockStructure.class, new BlockStructureLoader(plugin));
        addLoader(BlockChange.class, new BlockChangeLoader());

        final ActionLoader actions = this.getActionLoader();
        actions.addLoader("OPEN_GATE", (BranchLoader<Action>) branch ->
                new OpenGateAction());

        actions.addLoader("CLOSE_GATE", (BranchLoader<Action>) branch ->
                new CloseGateAction());

        actions.addLoader("SET_STAGE", (BranchLoader<Action>) branch ->
                new SetStageAction(branch.getInteger("stage", 1)));

    }

}
