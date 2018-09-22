package de.marcermarc.chunkloader.controller.world;

import de.marcermarc.chunkloader.controller.WorldController;
import de.marcermarc.chunkloader.model.Chunkloader;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigController {
    private static String CONFIG_NAME = "marcerChunkloader.yml";
    private static String CHUNKLOADER = "Chunkloader";

    private final WorldController controller;
    private YamlConfiguration configuration;
    private File configFile;

    public ConfigController(WorldController controller, World world) {
        this.controller = controller;

        this.configFile = new File(world.getWorldFolder(), CONFIG_NAME);
    }

    //region loadChunkloader
    public void load() {
        this.configuration = YamlConfiguration.loadConfiguration(configFile);

        loadChunkloader();
    }

    private void loadChunkloader() {
        List<String> chunkloader = configuration.getStringList(CHUNKLOADER);

        if (chunkloader != null) {
            for (String configString : chunkloader) {
                controller.getLoadedChunks().add(Chunkloader.fromConfig(controller.getWorld(), configString));
            }
        }
    }
    //endregion

    //region save
    public boolean save() {
        try {
            saveChunkloader();

            configuration.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveChunkloader() {
        configuration.set(CHUNKLOADER, controller.getLoadedChunks().getExistentChunkloader().stream().map(Chunkloader::toString).collect(Collectors.toList()));
    }
    //endregion
}
