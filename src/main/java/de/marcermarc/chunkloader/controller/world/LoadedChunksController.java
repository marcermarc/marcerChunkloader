package de.marcermarc.chunkloader.controller.world;

import de.marcermarc.chunkloader.model.Chunkloader;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LoadedChunksController {

    private final World world;
    private final HashSet<Chunkloader> existentChunkloader;
    private final Set<Chunkloader> existentChunkloaderUnmodifiable;
    private Set<Chunk> loadedChunksUnmodifiable;
    private Set<Location> chunkloaderBlocksUnmodifiable;
    private boolean inactual;

    public LoadedChunksController(World world) {
        this.world = world;
        this.existentChunkloader = new HashSet<>();
        this.existentChunkloaderUnmodifiable = Collections.unmodifiableSet(existentChunkloader);
        this.inactual = true;
    }

    private void update() {
        if (inactual) {
            HashSet<Chunk> loadedChunks = new HashSet<>();
            HashSet<Location> chunkloaderBlocks = new HashSet<>();

            for (Chunkloader chunkloader : existentChunkloader) {
                loadedChunks.addAll(chunkloader.getLoadedChunks());
                chunkloaderBlocks.addAll(chunkloader.getNeededBlocks());
            }

            this.loadedChunksUnmodifiable = Collections.unmodifiableSet(loadedChunks);
            this.chunkloaderBlocksUnmodifiable = Collections.unmodifiableSet(chunkloaderBlocks);

            this.inactual = false;
        }
    }

    public World getWorld() {
        return world;
    }

    /**
     * Unmodifyable
     * <p>
     * Use {@link LoadedChunksController#add(Chunkloader)} and {@link LoadedChunksController#removeWithNeededBlock(Location)} to modify this Set
     */
    public Set<Chunkloader> getExistentChunkloader() {
        return existentChunkloaderUnmodifiable;
    }

    /**
     * Unmodifyable
     * <p>
     * Filled by the {@link Chunkloader}
     */
    public Set<Chunk> getLoadedChunks() {
        update();
        return loadedChunksUnmodifiable;
    }

    /**
     * Unmodifyable
     * <p>
     * Filled by the {@link Chunkloader}
     */
    public Set<Location> getChunkloaderBlocks() {
        update();
        return chunkloaderBlocksUnmodifiable;
    }

    public void add(Chunkloader chunkloader) {
        inactual = true;
        this.existentChunkloader.add(chunkloader);
    }

    public void removeWithNeededBlock(Location location) {
        inactual = true;
        this.existentChunkloader.removeIf(c -> c.getNeededBlocks().contains(location));
    }
}
