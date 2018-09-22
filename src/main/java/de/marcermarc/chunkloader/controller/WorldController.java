package de.marcermarc.chunkloader.controller;

import de.marcermarc.chunkloader.controller.world.ConfigController;
import de.marcermarc.chunkloader.controller.world.LoadedChunksController;
import org.bukkit.World;

public class WorldController {
    private final World world;
    private final ConfigController config;
    private final LoadedChunksController loadedChunks;

    public WorldController(World world) {
        this.world = world;
        config = new ConfigController(this, world);
        loadedChunks = new LoadedChunksController(world);
    }

    public World getWorld() {
        return world;
    }

    public ConfigController getConfig() {
        return config;
    }

    public LoadedChunksController getLoadedChunks() {
        return loadedChunks;
    }
}
