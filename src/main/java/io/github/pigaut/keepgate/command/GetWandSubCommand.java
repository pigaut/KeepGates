package io.github.pigaut.keepgate.command;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GetWandSubCommand extends LangSubCommand {

    public GetWandSubCommand(@NotNull KeepGatePlugin plugin) {
        super("wand", plugin);
        withPlayerExecution((player, args, placeholders) -> {
            PlayerUtil.giveItemsOrDrop(player, GateTools.getWandTool());
            plugin.sendMessage(player, "received-wand", placeholders);
        });
    }

}
