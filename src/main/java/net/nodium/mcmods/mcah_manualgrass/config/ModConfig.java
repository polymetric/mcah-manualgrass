package net.nodium.mcmods.mcah_manualgrass.config;

public class ModConfig {
    public boolean noobBotMode;
    public String offsetsPath;
    public String uploadUrl;
    public String triggerPath;
    public float triggerDeleteDelay;

    public ModConfig() {}

    public String toString() {
        return String.format(
                "noobBotMode: %s\n" +
                "offsetsPath: %s\n" +
                "uploadUrl: %s\n" +
                "triggerPath: %s\n",
                "triggerDeleteDelay: %s\n",
                noobBotMode,
                offsetsPath,
                uploadUrl,
                triggerPath,
                triggerDeleteDelay
        );
    }
}
