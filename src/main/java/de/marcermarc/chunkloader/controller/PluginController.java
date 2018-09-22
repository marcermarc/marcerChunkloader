package de.marcermarc.chunkloader.controller;

import de.marcermarc.chunkloader.Main;
import org.bukkit.World;

import java.util.HashMap;

public class PluginController {
    private final ConfigController config;
    private final Main main;
    private final HashMap<World, WorldController> world;

    public PluginController(Main main) {
        this.main = main;
        this.config = new ConfigController(this);
        this.world = new HashMap<>();
    }

    public ConfigController getConfig() {
        return config;
    }

    public Main getMain() {
        return main;
    }

    public WorldController getWorld(World world) {
        return this.world.get(world);
    }

    public WorldController newWorld(World world) {
        WorldController controller = new WorldController(world);
        this.world.put(world, controller);
        return controller;
    }

    public WorldController removeWorld(World world) {
        return this.world.remove(world);
    }
}