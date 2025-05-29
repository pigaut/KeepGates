package io.github.pigaut.castlegate.util;

import io.github.pigaut.castlegate.*;
import io.github.pigaut.castlegate.gate.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class GateTools {

    private static final GatePlugin plugin = GatePlugin.getPlugin();
    public static final NamespacedKey GATE_KEY = new NamespacedKey("castlegate", "gate");

    public static @NotNull Rotation getToolRotation(@NotNull ItemStack item) {
        final GateTemplate gate = getGateFromTool(item);
        return gate != null ? gate.getRotation() : Rotation.NONE;
    }

    public static void incrementToolRotation(@NotNull ItemStack item) {
        final GateTemplate gate = getGateFromTool(item);
        if (gate == null) {
            return;
        }
        gate.setRotation(gate.getRotation().next());
        final ItemMeta templateMeta = plugin.getTools().getGateTool().getItemMeta();
        if (templateMeta != null) {
            PersistentData.setString(templateMeta, GATE_KEY, gate.getName());
            item.setItemMeta(templateMeta);
        }

        ItemPlaceholders.parseAll(item, gate);
    }

    public static @Nullable GateTemplate getGateFromTool(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        final String gateName = PersistentData.getString(meta, GATE_KEY);
        return plugin.getGateTemplate(gateName);
    }

    public static @NotNull ItemStack getGateTool(@NotNull GateTemplate gate) {
        final ItemStack gateItem = plugin.getTools().getGateTool();

        final BlockStructure lastStageStructure = gate.getLastStage().getStructure();
        final Material material = lastStageStructure.getBlockChanges().get(0).getType();
        gateItem.setType(Crops.isCrop(material) ? Crops.getCropSeeds(material) : material);

        final ItemMeta meta = gateItem.getItemMeta();
        if (meta != null) {
            PersistentData.setString(meta, GATE_KEY, gate.getName());
            gateItem.setItemMeta(meta);
        }

        return ItemPlaceholders.parseAll(gateItem, gate);
    }

}
