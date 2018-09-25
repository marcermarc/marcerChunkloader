package de.marcermarc.chunkloader;

import de.marcermarc.chunkloader.controller.ConfigController;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;

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

    public static void forceloadAdd(Player player, Chunk chunk) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                String.format("execute as %s run forceload add %d %d", player.getName(), chunk.getX() * 16, chunk.getZ() * 16));
    }

    public static void forceloadRemove(Player player, Chunk chunk) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                String.format("execute as %s run forceload remove %d %d", player.getName(), chunk.getX() * 16, chunk.getZ() * 16));
    }

    public static void forceloadRemoveAll(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                String.format("execute as %s run forceload remove all", player.getName()));
    }
}
