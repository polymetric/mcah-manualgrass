package net.nodium.mcmods.mcah_manualgrass.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.nodium.mcmods.mcah_manualgrass.ManualGrass;
import net.nodium.mcmods.mcah_manualgrass.Utils;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    public static ModConfig loadConfig(String path, ModConfig config) {
        File file = new File(path);

        // if config doesn't exist, create a default one
        try {
            if (!file.exists()) {
                // create default config
                file.createNewFile();

                Utils.writeStringToFile(
                        ManualGrass.CONFIG_PATH,
                        Utils.readResourceToString("/manualgrass-config.yml")
                );
            }
        } catch (IOException e) {
            Utils.sendPlayerChatError("couldn't create default config!");
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(file, ModConfig.class);
            Utils.sendPlayerChatMessage("loaded config");
        } catch (IOException e) {
            Utils.sendPlayerChatError("couldn't load manualgrass config!");
            e.printStackTrace();
        }
        return config;
    }
}
