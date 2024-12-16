package dr.mz27;

import dr.mz27.commands.MainCommand;
import dr.mz27.listeners.PlayerListener;
import dr.mz27.listeners.SleepProhibition;
import dr.mz27.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import dr.mz27.config.MainConfigManager;

public class Deathrain extends JavaPlugin {
    private SleepProhibition sleepProhibition;
    public static String prefix = "&8[&c&lDeathrain&8] ";
    private String version = getDescription().getVersion();
    private MainConfigManager mainConfigManager;

    @Override
    public void onEnable() {
        mainConfigManager = new MainConfigManager(this);
        sleepProhibition = new SleepProhibition(this);
        registerCommands();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage(
                MessageUtils.getColoredMessage(prefix + "&aDeathrain Enabled &fVersion: " + version));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                MessageUtils.getColoredMessage(prefix + "&aDeathrain Disabled &fVersion: " + version));
    }

    public void registerCommands() {
        getCommand("deathrain").setExecutor(new MainCommand(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public SleepProhibition getSleepProhibition() {
        return sleepProhibition;
    }

    public NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }
}
