package de.marcermarc.chunkloader.listener;

import de.marcermarc.chunkloader.Messages;
import de.marcermarc.chunkloader.Util;
import de.marcermarc.chunkloader.controller.PluginController;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ChunkloaderDeactivator implements Listener {
    private final PluginController controller;

    public ChunkloaderDeactivator(PluginController controller) {
        this.controller = controller;
    }

    //ToDo it is possible to build an irongolem, wither or snowman out of the base-blocks without destroying the chunkloader.

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        testChunkloaders(event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        testChunkloaders(event.getBlock(), null);
        event.blockList().forEach(b -> testChunkloaders(b, null));
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!controller.getConfig().getAllowedBaseBlocks().contains(event.getNewState().getType())) {
            testChunkloaders(event.getBlock(), null);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().forEach(b -> testChunkloaders(b, null));
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!controller.getConfig().getAllowedBaseBlocks().contains(event.getTo())) {
            testChunkloaders(event.getBlock(), null);
        }
    }

    private void testChunkloaders(Block block, Player player) {
        if (controller.getWorld(block.getWorld()).getLoadedChunks().getChunkloaderBlocks().contains(block.getLocation())) {
            controller.getWorld(block.getWorld()).getLoadedChunks().removeWithNeededBlock(block.getLocation());

            if (player != null) {
                player.sendMessage(Messages.getChunkloaderDestroyed());
            } else {
                block.getWorld().getNearbyEntities(block.getLocation(), 20, 20, 20).forEach(e -> e.sendMessage(Messages.getChunkloaderDestroyed()));
                player = block.getWorld().getPlayers().get(0);
            }

            if (player != null) {
                final Player p = player;

                Util.forceloadRemoveAll(player);
                controller.getWorld(block.getWorld()).getLoadedChunks().getLoadedChunks().forEach(c -> Util.forceloadAdd(p, c));
            }
        }
    }
}
