package de.marcermarc.chunkloader.controller;


import de.marcermarc.chunkloader.Util;
import org.bukkit.Material;

import java.util.*;

public class ConfigController {
    private static final String ALLOWED_ACTIVATION_ITEMS = "AllowedActivationItems";
    private static final String ALLOWED_BASE_BLOCKS = "AllowedBaseBlocks";
    private static final String MAX_TIER = "MaximumTier";
    private static final String DEBUG_MODE = "DebugMode";

    private final PluginController controller;

    private HashSet<Material> allowedActivationItems;
    private HashSet<Material> allowedBaseBlocks;
    private int maxTier;
    private boolean debugMode;

    public ConfigController(PluginController controller) {
        this.controller = controller;

        setDefaultConfig();

        loadAllowedActivationItems();
        loadAllowedBaseBlocks();
        loadLoadMaxTier();
        loadDebugMode();
    }

    private void setDefaultConfig() {
        controller.getMain().getConfig().addDefault(ALLOWED_ACTIVATION_ITEMS, Collections.singletonList(
                Util.materialToString(Material.HEART_OF_THE_SEA)
        ));
        controller.getMain().getConfig().addDefault(ALLOWED_BASE_BLOCKS, Arrays.asList(
                Util.materialToString(Material.IRON_BLOCK),
                Util.materialToString(Material.GOLD_BLOCK),
                Util.materialToString(Material.EMERALD_BLOCK),
                Util.materialToString(Material.DIAMOND_BLOCK)
        ));
        controller.getMain().getConfig().addDefault(MAX_TIER, 4);
        controller.getMain().getConfig().addDefault(DEBUG_MODE, false);

        controller.getMain().getConfig().options().copyDefaults(true);
        controller.getMain().saveDefaultConfig();
    }

    //region loadChunkloader
    public boolean loadConfig() {
        controller.getMain().reloadConfig();

        loadAllowedActivationItems();
        loadAllowedBaseBlocks();
        loadLoadMaxTier();
        loadDebugMode();

        return true;
    }

    private void loadAllowedActivationItems() {
        allowedActivationItems = new HashSet<>();
        List<String> allowedActivationStrings = controller.getMain().getConfig().getStringList(ALLOWED_ACTIVATION_ITEMS);
        for (String allowedActivationString : allowedActivationStrings) {
            allowedActivationItems.add(Util.stringToMaterial(allowedActivationString));
        }
    }

    private void loadAllowedBaseBlocks() {
        allowedBaseBlocks = new HashSet<>();
        List<String> allowedBaseBlocksStrings = controller.getMain().getConfig().getStringList(ALLOWED_BASE_BLOCKS);
        for (String allowedBaseBlockString : allowedBaseBlocksStrings) {
            allowedBaseBlocks.add(Util.stringToMaterial(allowedBaseBlockString));
        }
    }

    private void loadLoadMaxTier() {
        maxTier = controller.getMain().getConfig().getInt(MAX_TIER);
    }

    private void loadDebugMode() {
        debugMode = controller.getMain().getConfig().getBoolean(DEBUG_MODE);
    }
//endregion

    //region save
    public boolean saveConfig() {
        saveAllowedActivationItems();
        saveAllowedBaseBlocks();
        saveMaxTier();
        saveDebugMode();

        controller.getMain().saveConfig();

        return true;
    }

    private void saveAllowedActivationItems() {
        List<String> allowedActivationStrings = new ArrayList<>();
        for (Material material : allowedActivationItems) {
            allowedActivationStrings.add(Util.materialToString(material));
        }
        controller.getMain().getConfig().set(ALLOWED_ACTIVATION_ITEMS, allowedActivationStrings);
    }

    private void saveAllowedBaseBlocks() {
        List<String> allowedBaseBlocksStrings = new ArrayList<>();
        for (Material material : allowedBaseBlocks) {
            allowedBaseBlocksStrings.add(Util.materialToString(material));
        }
        controller.getMain().getConfig().set(ALLOWED_BASE_BLOCKS, allowedBaseBlocksStrings);
    }

    private void saveMaxTier() {
        controller.getMain().getConfig().set(MAX_TIER, maxTier);
    }

    private void saveDebugMode() {
        controller.getMain().getConfig().set(DEBUG_MODE, debugMode);
    }
    //endregion

    //region getters and setters
    public PluginController getController() {
        return controller;
    }

    public HashSet<Material> getAllowedActivationItems() {
        return allowedActivationItems;
    }

    public void setAllowedActivationItems(HashSet<Material> allowedActivationItems) {
        this.allowedActivationItems = allowedActivationItems;
    }

    public HashSet<Material> getAllowedBaseBlocks() {
        return allowedBaseBlocks;
    }

    public void setAllowedBaseBlocks(HashSet<Material> allowedBaseBlocks) {
        this.allowedBaseBlocks = allowedBaseBlocks;
    }

    public int getMaxTier() {
        return maxTier;
    }

    public void setMaxTier(int maxTier) {
        this.maxTier = maxTier;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    //endregion
}
