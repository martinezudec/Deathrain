package dr.mz27.config;

import dr.mz27.Deathrain;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfigManager {
    private CustomConfig configFile;
    private Deathrain plugin;
    private String worldName;
    private Boolean liteBans;

    public MainConfigManager(Deathrain plugin) {
        this.plugin = plugin;
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = configFile.getConfig();
        worldName = config.getString("config.world-name");
        liteBans = config.getBoolean("config.lite-bans");
    }

    public void reloadConfig() {
        configFile.reloadConfig();
        loadConfig();
    }

    public String getWorldName() {
        return worldName;
    }

    public Boolean getLiteBans() {
        return liteBans;
    }
}