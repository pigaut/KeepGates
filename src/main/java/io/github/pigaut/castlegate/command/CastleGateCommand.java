package io.github.pigaut.castlegate.command;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.command.gate.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.core.item.command.*;
import io.github.pigaut.voxel.core.message.command.*;
import io.github.pigaut.voxel.core.particle.command.*;
import io.github.pigaut.voxel.core.sound.command.*;
import io.github.pigaut.voxel.core.structure.command.*;
import io.github.pigaut.voxel.plugin.command.*;
import org.jetbrains.annotations.*;

public class CastleGateCommand extends EnhancedCommand {

    public CastleGateCommand(@NotNull GatePlugin plugin) {
        super(plugin, "castlegate");
        this.description = "CastleGate plugin commands";
        this.setAliases("cgate", "cg");

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
