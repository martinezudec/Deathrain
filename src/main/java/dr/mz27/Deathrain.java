package dr.mz27;

import dr.mz27.commands.MainCommand;
import dr.mz27.listeners.PlayerListener;
import dr.mz27.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Deathrain extends JavaPlugin {

    public static String prefix = "&8[&c&lDeathrain&8] ";
    private String version = getDescription().getVersion();

    public void onEnable() {
        registerCommands();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage(
                MessageUtils.getColoredMessage(prefix + "&aDeathrain Enabled &fVersion: " + version));
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                MessageUtils.getColoredMessage(prefix + "&aDeathrain Disabled &fVersion: " + version));
    }

    public void registerCommands() {
        getCommand("deathrain").setExecutor(new MainCommand(this));
    }
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
}
