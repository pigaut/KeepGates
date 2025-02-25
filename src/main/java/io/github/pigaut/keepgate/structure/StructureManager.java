package io.github.pigaut.keepgate.structure;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class StructureManager extends Manager {

    private final Map<String, BlockStructure> structuresByName = new ConcurrentHashMap<>();
    private final Set<Material> blockBlacklist = new HashSet<>();

    public StructureManager(EnhancedPlugin plugin) {
        super(plugin);
    }

    public List<String> getStructureNames() {
        return new ArrayList<>(structuresByName.keySet());
    }

    public @Nullable BlockStructure getBlockStructure(String name) {
        return structuresByName.get(name);
    }

    public void registerBlockStructure(String name, BlockStructure structure) {
        structuresByName.put(name, structure);
    }

    public BlockStructure removeBlockStructure(String name) {
        return structuresByName.remove(name);
    }

    public Set<Material> getBlockBlacklist() {
        return new HashSet<>(blockBlacklist);
    }

    public boolean isBlacklisted(Material material) {
        return blockBlacklist.contains(material);
    }

    @Override
    public void loadData() {
        structuresByName.clear();
        for (File structureFile : plugin.getFiles("structures")) {
            final RootSequence config = plugin.loadConfigSequence(structureFile);
            registerBlockStructure(config.getName(), config.load(BlockStructure.class));
        }

        blockBlacklist.clear();
        blockBlacklist.addAll(plugin.getConfiguration().getList("structure-save-blacklist", Material.class));
    }

}
