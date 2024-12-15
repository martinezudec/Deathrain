package dr.mz27.commands;

import dr.mz27.Deathrain;
import dr.mz27.listeners.PlayerListener;
import dr.mz27.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    help(sender);
                    break;
                case "get":
                    subCommandGet(sender, args);
                    break;
                case "stop":
                    PlayerListener.stopStorm(plugin);
                    sender.sendMessage(MessageUtils.getColoredMessage(
                            Deathrain.prefix + "&7The storm has been stopped"));
                    break;
                case "start":
                    if (args.length == 2) {
                        try {
                            int durationMinutes = Integer.parseInt(args[1]);
                            long durationTicks = durationMinutes * 60L * 20L; // Convertir minutos a ticks
                            PlayerListener listener = new PlayerListener(plugin);
                            listener.startStorm(durationTicks);
                            sender.sendMessage(MessageUtils.getColoredMessage(
                                    Deathrain.prefix + "&aThe storm has started for " + durationMinutes + " minutes."));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(MessageUtils.getColoredMessage(
                                    Deathrain.prefix + "&cInvalid duration. Please enter a number."));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.getColoredMessage(
                                Deathrain.prefix + "&cPlease specify the duration in minutes."));
                    }
                    break;
                default:
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
        sender.sendMessage(MessageUtils.getColoredMessage("&6/deathrain start <minutes> &f- &7Start the storm for the specified duration in minutes"));
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
}