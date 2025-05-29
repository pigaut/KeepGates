package io.github.pigaut.castlegate.config;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.action.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

public class GateConfigurator extends PluginConfigurator {

    public GateConfigurator(@NotNull GatePlugin plugin) {
        super(plugin);
        addLoader(GateTemplate.class, new GateLoader());

        final ActionLoader actions = this.getActionLoader();
        actions.addLoader("OPEN_GATE", (BranchLoader<Action>) branch ->
                new OpenGateAction());

        actions.addLoader("CLOSE_GATE", (BranchLoader<Action>) branch ->
                new CloseGateAction());

        actions.addLoader("SET_STAGE", (BranchLoader<Action>) branch ->
                new SetStageAction(branch.getInteger("stage", 1)));

    }

}
