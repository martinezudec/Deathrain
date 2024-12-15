package dr.mz27.commands;

import dr.mz27.Deathrain;
import dr.mz27.listeners.PlayerListener;
import dr.mz27.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class MainCommand implements CommandExecutor {
    private Deathrain plugin;
    public MainCommand(Deathrain plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cYou must be in game to use this command"));
            return true;
        } else if (!sender.hasPermission("deathrain.commands")) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cYou don't have permission to use this command"));
            return true;
        }

        if (args.length >= 1) {
            switch (args[0]) {
                case "help":
                    // /dr help
                    help(sender);
                    break;
                case "get":
                    // /dr get <author/version>
                    subCommandGet(sender, args);
                    break;
                case "stop":
                    // /dr stop
                    stopStorm(sender);
                    break;
                default:
                    // /dr
                    sender.sendMessage(MessageUtils.getColoredMessage(
                            Deathrain.prefix + "&6Type '/deathrain help' for help"));
            }
        } else {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&6Type '/deathrain help' for help"));
        }
        return true;
    }
    public void help(CommandSender sender) {
        sender.sendMessage(MessageUtils.getColoredMessage("&d&l<-----------------Commands----------------->"));
        sender.sendMessage(MessageUtils.getColoredMessage("&6You can type '/dr' instead of '/deathrain'"));
        sender.sendMessage(MessageUtils.getColoredMessage("&6/deathrain help &f- &7Shows this help message"));
        sender.sendMessage(MessageUtils.getColoredMessage("&6/deathrain get <author/version> &f- &7Get author/version of the plugin"));
        sender.sendMessage(MessageUtils.getColoredMessage("&6/deathrain stop &f- &7Stop the storm"));
        sender.sendMessage(MessageUtils.getColoredMessage("&d&l<-----------------Commands----------------->"));
    }
    public void subCommandGet(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cPlease specify the author/version"));
            return;
        }
        switch (args[1]) {
            case "author":
                sender.sendMessage(MessageUtils.getColoredMessage(
                        Deathrain.prefix + "&fAuthor: &e" + plugin.getDescription().getAuthors()));
                break;
            case "version":
                sender.sendMessage(MessageUtils.getColoredMessage(
                        Deathrain.prefix + "&fVersion: &b" + plugin.getDescription().getVersion()));
                break;
            default:
                sender.sendMessage(MessageUtils.getColoredMessage(
                        Deathrain.prefix + "&cPlease specify the author/version"));
        }
    }

    public void stopStorm(CommandSender sender) {
        World overworld = Bukkit.getWorld("world");
        if (overworld == null) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cOverworld not found. Make sure the main world name is correct."));
            return;
        }

        if (PlayerListener.stormTask != null) {
            PlayerListener.stormTask.cancel();
            PlayerListener.stormTask = null;
        }

        overworld.setStorm(false);
        PlayerListener.remainingTime = 0L;

        sender.sendMessage(MessageUtils.getColoredMessage(
                Deathrain.prefix + "&aThe storm has been stopped"));
    }
}
