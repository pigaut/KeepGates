package io.github.pigaut.keepgate.gate.template;

import io.github.pigaut.keepgate.stage.*;
import io.github.pigaut.keepgate.util.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;

import java.util.*;

public class GateTemplate implements PlaceholderSupplier {

    private final String name;
    private final List<GateStage> stages;
    private Rotation rotation = Rotation.NONE;

    public GateTemplate(String name, List<GateStage> stages) {
        this.name = name;
        this.stages = stages;
    }

    public String getName() {
        return name;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public ItemStack getItem() {
        return GateTools.getGeneratorTool(this);
    }

    public int getMaxStage() {
        return stages.size() - 1;
    }

    public List<GateStage> getStages() {
        return new ArrayList<>(stages);
    }

    public GateStage getStage(int stage) {
        return stages.get(stage);
    }

    public GateStage getLastStage() {
        return stages.get(getMaxStage());
    }

    public int indexOfStage(GateStage stage) {
        final int index = stages.indexOf(stage);
        if (index == -1) {
            throw new IllegalArgumentException("Generator does not contain that stage");
        }
        return index;
    }

    public int getStageFromStructure(Location origin, Rotation rotation) {
        int currentStage = getMaxStage();
        for (int i = getMaxStage(); i >= 0; i--) {
            final GateStage stage = getStage(i);
            if (stage.getStructure().matchBlocks(origin, rotation)) {
                currentStage = i;
                break;
            }
        }
        return currentStage;
    }

    public Set<Block> getAllOccupiedBlocks(Location location, Rotation rotation) {
        final Set<Block> blocks = new HashSet<>();
        for (GateStage stage : stages) {
            blocks.addAll(stage.getStructure().getBlocks(location, rotation));
        }
        return blocks;
    }

    public Placeholder[] getPlaceholders() {
        return new Placeholder[]{
                Placeholder.of("%gate%", name),
                Placeholder.of("%gate_stages%", stages),
                Placeholder.of("%gate_rotation%", rotation)
        };
    }

    @Override
    public String toString() {
        return "Generator{" + "name='" + name + '\'' + ", stages=" + stages + '}';
    }

}
