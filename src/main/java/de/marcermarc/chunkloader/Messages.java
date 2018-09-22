package de.marcermarc.chunkloader;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Messages {
    private static final String MESSAGE_PREFIX = ChatColor.DARK_GREEN + "[marcerChunkloader]" + ChatColor.RESET + " ";
    private static final String MESSAGE_PREFIX_SYSOUT = "[marcerChunkloader] ";
    private static final String INFO = MESSAGE_PREFIX + "marcerChunkloader is a plugin that allows you to build chunkloader out of beacons.";
    private static final String NOT_ALLOWED_CONFIG = MESSAGE_PREFIX + ChatColor.RED + "You are nor allowed to change the config.";
    private static final String LOAD_OK = MESSAGE_PREFIX + "Configuration loaded.";
    private static final String LOAD_FAILED = MESSAGE_PREFIX + ChatColor.RED + "Loading the configuration failed.";
    private static final String SAVE_OK = MESSAGE_PREFIX + "Configuration saved.";
    private static final String SAVE_FAILED = MESSAGE_PREFIX + ChatColor.RED + "Saving the configuration failed.";
    private static final String BASE_BLOCKS_LIST = MESSAGE_PREFIX + "The following base blocks are for the chunkloader allowed: %s";
    private static final String BASE_BLOCKS_ADD_OK = MESSAGE_PREFIX + "Block %s is now allowed.";
    private static final String BASE_BLOCKS_ADD_DUPLICATE = MESSAGE_PREFIX + "Block %s is already in the list.";
    private static final String BASE_BLOCKS_REMOVE_OK = MESSAGE_PREFIX + "Block %s is now removed.";
    private static final String BASE_BLOCKS_REMOVE_DUPLICATE = MESSAGE_PREFIX + "Block %s is not in the list.";
    private static final String NOT_A_BLOCK = MESSAGE_PREFIX + "This isn't a block.";
    private static final String ACTIVATION_ITEMS_LIST = MESSAGE_PREFIX + "The following items activate the chunkloader: %s";
    private static final String ACTIVATION_ITEMS_ADD_OK = MESSAGE_PREFIX + "Item %s is now allowed.";
    private static final String ACTIVATION_ITEMS_ADD_DUPLICATE = MESSAGE_PREFIX + "Item %s is already in the list.";
    private static final String ACTIVATION_ITEMS_REMOVE_OK = MESSAGE_PREFIX + "Item %s is now removed.";
    private static final String ACTIVATION_ITEMS_REMOVE_DUPLICATE = MESSAGE_PREFIX + "Item %s is not in the list.";
    private static final String NOT_A_ITEM = MESSAGE_PREFIX + "This isn't an item.";
    private static final String MAX_TIER = MESSAGE_PREFIX + "The maximum tier is %d";
    private static final String MAX_TIER_OK = MESSAGE_PREFIX + "The maximum tier is now %d";
    private static final String NOT_NUMBER = MESSAGE_PREFIX + "This is not a number";
    private static final String DEBUG_TRUE = MESSAGE_PREFIX + "Debug is on.";
    private static final String DEBUG_FALSE = MESSAGE_PREFIX + "Debug is off.";
    private static final String DEBUG_NOW_TRUE = MESSAGE_PREFIX + "Debug is now on.";
    private static final String DEBUG_NOW_FALSE = MESSAGE_PREFIX + "Debug is now off.";
    private static final String NOT_BOOLEAN = MESSAGE_PREFIX + "This is not a boolean.";
    private static final String CHUNK_LOADED = MESSAGE_PREFIX + "This chunk is in range of a chunkloader.";
    private static final String CHUNK_NOT_LOADED = MESSAGE_PREFIX + "This chunk is " + ChatColor.BOLD + "not" + ChatColor.RESET + " in range of a chunkloader.";
    private static final String CHUNKLOADER_AKTIVATED = MESSAGE_PREFIX + ChatColor.BOLD + "Chunkloader activated!" + ChatColor.RESET + " %d Chunks are loaded.";
    private static final String CHUNKLOADER_DESTROYED = MESSAGE_PREFIX + ChatColor.BOLD + "Chunkloader destroyed!" + ChatColor.RESET + " A block of a chunkloader is destroyed.";
    private static final String CHUNKLOADER_OPEN = MESSAGE_PREFIX + ChatColor.BOLD + "Chunkloader!" + ChatColor.RESET + " You have opened a tier %d chunkloader.";
    private static final String JOIN = MESSAGE_PREFIX + "Welcome back. You have %d avtice chunkloaders.";
    private static final String WORLD_LOADED = MESSAGE_PREFIX_SYSOUT + "World '%s' found, %d Chunks loaded.";
    private static final String WORLD_UNLOADED = MESSAGE_PREFIX_SYSOUT + "World '%s' unloaded.";
    private static final String WORLD_SAVED = MESSAGE_PREFIX_SYSOUT + "World '%s' saved";
    private static final String WORLD_CHUNK_KEEP_LOADED = MESSAGE_PREFIX_SYSOUT + "Cancelled unloading of chunk at position [%d|%d] in world '%s'.";

    private Messages() {
    }

    public static String getInfo() {
        return INFO;
    }

    public static String getNotAllowedConfig() {
        return NOT_ALLOWED_CONFIG;
    }

    public static String getLoadOk() {
        return LOAD_OK;
    }

    public static String getLoadFailed() {
        return LOAD_FAILED;
    }

    public static String getSaveOk() {
        return SAVE_OK;
    }

    public static String getSaveFailed() {
        return SAVE_FAILED;
    }

    public static String getBaseBlocksList(Material[] allowedBaseBlocks) {
        return String.format(BASE_BLOCKS_LIST, Util.materialListToString(allowedBaseBlocks));
    }

    public static String getBaseBlocksAddOk(Material material) {
        return String.format(BASE_BLOCKS_ADD_OK, Util.materialToString(material));
    }

    public static String getBaseBlocksAddDuplicate(Material material) {
        return String.format(BASE_BLOCKS_ADD_DUPLICATE, Util.materialToString(material));
    }

    public static String getBaseBlocksRemoveOk(Material material) {
        return String.format(BASE_BLOCKS_REMOVE_OK, Util.materialToString(material));
    }

    public static String getBaseBlocksRemoveDuplicate(Material material) {
        return String.format(BASE_BLOCKS_REMOVE_DUPLICATE, Util.materialToString(material));
    }

    public static String getNotABlock() {
        return NOT_A_BLOCK;
    }

    public static String getActivationItemsList(Material[] allowedBaseBlocks) {
        return String.format(ACTIVATION_ITEMS_LIST, Util.materialListToString(allowedBaseBlocks));
    }

    public static String getActivationItemsAddOk(Material material) {
        return String.format(ACTIVATION_ITEMS_ADD_OK, Util.materialToString(material));
    }

    public static String getActivationItemsAddDuplicate(Material material) {
        return String.format(ACTIVATION_ITEMS_ADD_DUPLICATE, Util.materialToString(material));
    }

    public static String getActivationItemsRemoveOk(Material material) {
        return String.format(ACTIVATION_ITEMS_REMOVE_OK, Util.materialToString(material));
    }

    public static String getActivationItemsRemoveDuplicate(Material material) {
        return String.format(ACTIVATION_ITEMS_REMOVE_DUPLICATE, Util.materialToString(material));
    }

    public static String getNotAItem() {
        return NOT_A_ITEM;
    }

    public static String getMaxTier(int maxTier) {
        return String.format(MAX_TIER, maxTier);
    }

    public static String getMaxTierOk(int maxTier) {
        return String.format(MAX_TIER_OK, maxTier);
    }

    public static String getNotNumber() {
        return NOT_NUMBER;
    }

    public static String getDebugTrue() {
        return DEBUG_TRUE;
    }

    public static String getDebugFalse() {
        return DEBUG_FALSE;
    }

    public static String getDebugNowTrue() {
        return DEBUG_NOW_TRUE;
    }

    public static String getDebugNowFalse() {
        return DEBUG_NOW_FALSE;
    }

    public static String getNotBoolean() {
        return NOT_BOOLEAN;
    }

    public static String getChunkLoaded() {
        return CHUNK_LOADED;
    }

    public static String getChunkNotLoaded() {
        return CHUNK_NOT_LOADED;
    }


    public static String getChunkloaderAktivated(int size) {
        return String.format(CHUNKLOADER_AKTIVATED, size);
    }

    public static String getChunkloaderDestroyed() {
        return CHUNKLOADER_DESTROYED;
    }

    public static String getChunkloaderOpened(int tier) {
        return String.format(CHUNKLOADER_OPEN, tier);
    }

    public static String getJoin(long amount) {
        return String.format(JOIN, amount);
    }


    public static String getWorldLoaded(String name, int chunkAmound) {
        return String.format(WORLD_LOADED, name, chunkAmound);
    }

    public static String getWorldUnloaded(String name) {
        return String.format(WORLD_UNLOADED, name);
    }

    public static String getWorldSaved(String name) {
        return String.format(WORLD_SAVED, name);
    }

    public static String getWorldChunkKeepLoaded(String name, int positionX, int positionZ) {
        return String.format(WORLD_CHUNK_KEEP_LOADED, positionX, positionZ, name);
    }
}
