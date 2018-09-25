package de.marcermarc.chunkloader.listener;

import de.marcermarc.chunkloader.Messages;
import de.marcermarc.chunkloader.controller.PluginController;
import de.marcermarc.chunkloader.controller.WorldController;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldLoadedUnloaded implements Listener {
    private final PluginController controller;

    public WorldLoadedUnloaded(PluginController controller) {
        this.controller = controller;

        controller.getMain().getServer().getWorlds().forEach(this::loadWorld);
    }

    //region load
    @EventHandler
    public void onWorldLoaded(WorldLoadEvent event) {
        loadWorld(event.getWorld());
    }

    private void loadWorld(World world) {
        WorldController worldController = controller.newWorld(world);

        worldController.getConfig().load();

        worldController.getLoadedChunks().getLoadedChunks().forEach(c -> {
            if (!c.isLoaded()) c.load(false);
        });

        System.out.print(Messages.getWorldLoaded(world.getName(), worldController.getLoadedChunks().getLoadedChunks().size()));
    }
    //endregion

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        controller.getWorld(event.getWorld()).getConfig().save();

        if (controller.getConfig().isDebugMode()) {
            System.out.print(Messages.getWorldSaved(event.getWorld().getName()));
        }
    }

    //region unload
    @EventHandler
    public void onWorldUnloaded(WorldUnloadEvent event) {
        unloadWorld(event.getWorld());
        System.out.print(Messages.getWorldUnloaded(event.getWorld().getName()));
    }

    public void unloadAllWorlds() {
        controller.getMain().getServer().getWorlds().forEach(this::unloadWorld);
    }

    private void unloadWorld(World world) {
        WorldController worldController = controller.removeWorld(world);

        worldController.getConfig().save();
    }
    //endregion

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        long amount = Bukkit.getServer().getWorlds().stream().mapToLong(
                world -> controller.getWorld(world).getLoadedChunks().getExistentChunkloader().stream()
                        .filter(chunkloader -> chunkloader.getCreator().equals(event.getPlayer().getUniqueId()))
                        .count()
        ).sum();

        if (amount > 0) {
            event.getPlayer().sendMessage(Messages.getJoin(amount));


        }
    }

    @EventHandler
    public void onChunkUnloaded(ChunkUnloadEvent event) {
        WorldController worldController = controller.getWorld(event.getWorld());

        if (worldController != null && worldController.getLoadedChunks().getLoadedChunks().contains(event.getChunk())) {
            if (controller.getConfig().isDebugMode()) {
                System.out.print(Messages.getWorldChunkKeepLoaded(event.getWorld().getName(), event.getChunk().getX(), event.getChunk().getZ()));
            }

            event.setCancelled(true);
        }
    }


}
