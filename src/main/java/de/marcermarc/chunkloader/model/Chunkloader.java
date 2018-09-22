package de.marcermarc.chunkloader.model;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.UUID;

public class Chunkloader {
    private final UUID creator;
    private final int tier;
    private final Beacon beacon;
    private final HashSet<Location> neededBlocks;
    private final HashSet<Chunk> loadedChunks;

    /**
     * to created a new chunkloder
     */
    public Chunkloader(UUID creator, int tier, Beacon beacon) {
        this.creator = creator;
        this.tier = tier;
        this.beacon = beacon;
        this.neededBlocks = loadNeededBlock();
        this.loadedChunks = loadLoadedChunks();
    }

    /**
     * To loadChunkloader an Chunkloader by config
     */
    public static Chunkloader fromConfig(World world, String config) {
        if (config == null) {
            return null;
        }

        String[] split = config.split(";");

        if (split.length != 5) {
            return null;
        }

        UUID creator = UUID.fromString(split[0]);
        int tier = Integer.parseInt(split[1]);
        int x = Integer.parseInt(split[2]);
        int y = Integer.parseInt(split[3]);
        int z = Integer.parseInt(split[4]);

        BlockState blockState = world.getBlockAt(x, y, z).getState();

        if (!(blockState instanceof Beacon)) {
            return null;
        }

        return new Chunkloader(creator, tier, (Beacon) blockState);
    }

    private HashSet<Chunk> loadLoadedChunks() {
        HashSet<Chunk> loadedChunks = new HashSet<>();

        World world = beacon.getWorld();
        int chunkX = beacon.getChunk().getX();
        int chunkZ = beacon.getChunk().getZ();

        for (int x = -tier; x <= tier; x++) {
            for (int z = -tier; z <= tier; z++) {
                loadedChunks.add(world.getChunkAt(chunkX + x, chunkZ + z));
            }
        }

        return loadedChunks;
    }

    private HashSet<Location> loadNeededBlock() {
        HashSet<Location> neededBlocks = new HashSet<>();
        neededBlocks.add(beacon.getLocation());

        World world = beacon.getWorld();
        int beaconX = beacon.getX();
        int beaconY = beacon.getY();
        int beaconZ = beacon.getZ();

        for (int level = 1; level <= tier; level++) {
            for (int x = -level; x <= level; x++) {
                for (int z = -level; z <= level; z++) {
                    neededBlocks.add(new Location(world, beaconX + x, beaconY - level, beaconZ + z));
                }
            }
        }
        return neededBlocks;
    }

    public UUID getCreator() {
        return creator;
    }

    public int getTier() {
        return tier;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public HashSet<Location> getNeededBlocks() {
        return neededBlocks;
    }

    public HashSet<Chunk> getLoadedChunks() {
        return loadedChunks;
    }

    @Override
    public String toString() {
        return String.format("%s;%d;%d;%d;%d", creator.toString(), tier, beacon.getBlock().getX(), beacon.getBlock().getY(), beacon.getBlock().getZ());
    }
}
