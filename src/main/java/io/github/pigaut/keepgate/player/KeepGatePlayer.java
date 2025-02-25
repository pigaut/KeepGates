package io.github.pigaut.keepgate.player;

import io.github.pigaut.voxel.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class KeepGatePlayer extends AbstractPluginPlayer {

    private Location firstSelection = null, secondSelection = null;

    public KeepGatePlayer(UUID playerId) {
        super(playerId);
    }

    public KeepGatePlayer(Player player) {
        super(player);
    }

    public Location getFirstSelection() {
        return firstSelection;
    }

    public Location getSecondSelection() {
        return secondSelection;
    }

    public void setFirstSelection(Location firstSelection) {
        this.firstSelection = firstSelection;
    }

    public void setSecondSelection(Location secondSelection) {
        this.secondSelection = secondSelection;
    }

}
