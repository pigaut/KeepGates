package io.github.pigaut.keepgate.command;

import io.github.pigaut.keepgate.*;
import io.github.pigaut.keepgate.command.generator.*;
import io.github.pigaut.keepgate.command.structure.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.item.command.*;
import io.github.pigaut.voxel.message.command.*;
import io.github.pigaut.voxel.particle.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import io.github.pigaut.voxel.sound.command.*;
import org.jetbrains.annotations.*;

public class KeepGateCommand extends EnhancedCommand {

    public KeepGateCommand(@NotNull KeepGatePlugin plugin) {
        super(plugin, "keepgate");
        this.description = "KeepGate plugin commands";
        this.setAliases("kgate");

        addSubCommand(new HelpSubCommand(plugin));
        addSubCommand(new ReloadSubCommand(plugin));
        addSubCommand(new ItemSubCommand(plugin));
        addSubCommand(new ParticleSubCommand(plugin));
        addSubCommand(new MessageSubCommand(plugin));
        addSubCommand(new SoundSubCommand(plugin));
        addSubCommand(new GateSubCommand(plugin));
        addSubCommand(new StructureSubCommand(plugin));
        addSubCommand(new GetWandSubCommand(plugin));
    }

}
