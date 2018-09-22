package de.marcermarc.chunkloader;

import de.marcermarc.chunkloader.controller.ConfigController;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Beacon;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    private Util() {
    }

    public static String materialToString(Material material) {
        return NamespacedKey.MINECRAFT + ":" + material.toString().toLowerCase();
    }

    public static String materialListToString(Material[] materials) {
        if (materials == null || materials.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder(Util.materialToString(materials[0]));
        for (int i = 1; i < materials.length; i++) {
            builder.append(", ").append(Util.materialToString(materials[i]));
        }
        return builder.toString();
    }

    public static Material stringToMaterial(String material) {
        return Material.matchMaterial(material);
    }

    public static List<String> tabCompleteFilter(List<String> full, String startetText) {
        return full.stream().filter(s -> startetText.isEmpty() || s.contains(startetText)).collect(Collectors.toList());
    }

    public static int getTier(Beacon beacon, ConfigController controller) {
        World world = beacon.getWorld();
        int beaconX = beacon.getX();
        int beaconY = beacon.getY();
        int beaconZ = beacon.getZ();

        int tier = 0;

        while (tier <= controller.getMaxTier()) {
            tier++;

            for (int x = -tier; x <= tier; x++) {
                for (int z = -tier; z <= tier; z++) {
                    if (!controller.getAllowedBaseBlocks().contains(world.getBlockAt(beaconX + x, beaconY - tier, beaconZ + z).getType())) {
                        return tier - 1;
                    }
                }
            }
        }
        return tier;
    }
}
