package net.nodium.mcmods.mcah_manualgrass.config;

public class ModConfig {
    public boolean noobBotMode;
    public String offsetsPath;
    public String uploadUrl;
    public String triggerPath;

    public ModConfig() {}

    public String toString() {
        return String.format(
                "noobBotMode: %s\n" +
                "offsetsPath: %s\n" +
                "uploadUrl: %s\n" +
                "triggerPath: %s\n",
                noobBotMode,
                offsetsPath,
                uploadUrl,
                triggerPath
        );
    }
}
