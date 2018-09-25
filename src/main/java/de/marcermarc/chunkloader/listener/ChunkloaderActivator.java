package de.marcermarc.chunkloader.listener;

import de.marcermarc.chunkloader.Messages;
import de.marcermarc.chunkloader.Util;
import de.marcermarc.chunkloader.controller.PluginController;
import de.marcermarc.chunkloader.model.Chunkloader;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

/**
 * This class controls the beacon / chunkloader Activation
 */
public class ChunkloaderActivator implements Listener {
    private final PluginController controller;

    private HashSet<HumanEntity> hasAllowedItemInBeacon = new HashSet<>();

    public ChunkloaderActivator(PluginController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof BeaconInventory) {
            controller.getWorld(event.getInventory().getLocation().getWorld()).getLoadedChunks().getExistentChunkloader().stream()
                    .filter(c -> c.getBeacon().getLocation().equals(event.getInventory().getLocation())).findFirst()
                    .ifPresent(chunkloader -> event.getPlayer().sendMessage(Messages.getChunkloaderOpened(chunkloader.getTier())));
        }

    }

    /**
     * Allows the player to put {@link de.marcermarc.chunkloader.controller.ConfigController#allowedActivationItems} into a Beacon Slot
     * <p>
     * Tests if the player has an {@link de.marcermarc.chunkloader.controller.ConfigController#allowedActivationItems} in a Beacon-Slot and save this in {@link ChunkloaderActivator#hasAllowedItemInBeacon}.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() >= 0 && event.getInventory() instanceof BeaconInventory) {
            if (controller.getConfig().getAllowedActivationItems().contains(event.getCurrentItem().getType())
                    || controller.getConfig().getAllowedActivationItems().contains(event.getCursor().getType())) {

                ItemStack cursor = event.getCursor();
                ItemStack current = event.getCurrentItem();

                switch (event.getAction()) {
                    case MOVE_TO_OTHER_INVENTORY:
                        // Shift Clicked

                        if (controller.getConfig().getAllowedActivationItems().contains(current.getType())
                                && current.getAmount() == 1
                                && !(event.getClickedInventory() instanceof BeaconInventory)
                                && (((BeaconInventory) event.getInventory()).getItem() == null
                                || ((BeaconInventory) event.getInventory()).getItem().getType() == Material.AIR)) {
                            ((BeaconInventory) event.getInventory()).setItem(current);
                            event.setCurrentItem(null);
                            event.setCancelled(true);
                        }

                        break;
                    case PLACE_ALL:
                        // Left Click with Item on clear Spot
                    case PLACE_ONE:
                        // Right Click with Item on clear Spot
                        if (event.getClickedInventory() instanceof BeaconInventory) {
                            event.setCurrentItem(new ItemStack(cursor.getType()));
                            cursor.setAmount(cursor.getAmount() - 1);
                            event.setCancelled(true);
                        }
                        break;

                    case NOTHING:
                        // On Beacon: Left or Right Click with an unsupport Item on filled Slot
                        if (event.getClickedInventory() instanceof BeaconInventory && cursor.getAmount() == 1) {
                            event.setCurrentItem(cursor);
                            event.setCursor(current);
                            event.setCancelled(true);
                        }
                        break;
                }
            }

            if (((BeaconInventory) event.getInventory()).getItem() != null
                    && controller.getConfig().getAllowedActivationItems().contains(((BeaconInventory) event.getInventory()).getItem().getType())) {
                hasAllowedItemInBeacon.add(event.getWhoClicked());
            } else {
                hasAllowedItemInBeacon.remove(event.getWhoClicked());
            }
        }

    }

    /**
     * Test if a beacon was activated with a an item of {@link de.marcermarc.chunkloader.controller.ConfigController#allowedActivationItems}
     * <p>
     * If a beacon is activated the beacon slot is cleared before this event is called.
     * So the method prove the value of {@link ChunkloaderActivator#hasAllowedItemInBeacon} and the slot of the beacon.
     * If both is ok the chunkloader will be activated.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof BeaconInventory
                && hasAllowedItemInBeacon.contains(event.getPlayer())
                && ((BeaconInventory) event.getInventory()).getItem() == null
                && controller.getWorld(event.getInventory().getLocation().getWorld()).getLoadedChunks().getExistentChunkloader()
                .stream().noneMatch(x -> x.getBeacon().getLocation().equals(event.getInventory().getLocation()))) {

            Beacon beacon = (Beacon) event.getInventory().getLocation().getBlock().getState();
            int tier = Util.getTier(beacon, controller.getConfig());
            Chunkloader chunkloader = new Chunkloader(event.getPlayer().getUniqueId(), tier, beacon);
            controller.getWorld(beacon.getWorld()).getLoadedChunks().add(chunkloader);

            chunkloader.getLoadedChunks().forEach(c -> {
                if (!c.isLoaded()) c.load(false);
                Util.forceloadAdd((Player) event.getPlayer(), c);
            });

            event.getPlayer().sendMessage(Messages.getChunkloaderAktivated(chunkloader.getLoadedChunks().size()));

            hasAllowedItemInBeacon.remove(event.getPlayer());
        }
    }
}