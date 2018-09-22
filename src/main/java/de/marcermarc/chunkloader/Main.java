package de.marcermarc.chunkloader;

import de.marcermarc.chunkloader.controller.PluginController;
import de.marcermarc.chunkloader.listener.ChunkloaderActivator;
import de.marcermarc.chunkloader.listener.ChunkloaderDeactivator;
import de.marcermarc.chunkloader.listener.Command;
import de.marcermarc.chunkloader.listener.WorldLoadedUnloaded;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PluginController controller;
    private WorldLoadedUnloaded worldLoadedUnloaded;

    @Override
    public void onEnable() {
        controller = new PluginController(this);

        PluginManager pM = getServer().getPluginManager();

        registerEvents(pM);
    }

    private void registerEvents(PluginManager in_PM) {
        worldLoadedUnloaded = new WorldLoadedUnloaded(controller);

        in_PM.registerEvents(new ChunkloaderActivator(controller), this);
        in_PM.registerEvents(new ChunkloaderDeactivator(controller), this);
        in_PM.registerEvents(worldLoadedUnloaded, this);

        Command c = new Command(controller);
        in_PM.registerEvents(c, this);

        this.getCommand("marcerChunkloader").setExecutor(c);
        this.getCommand("marcerChunkloader").setTabCompleter(c);
    }

    @Override
    public void onDisable() {
        worldLoadedUnloaded.unloadAllWorlds();
    }

}
