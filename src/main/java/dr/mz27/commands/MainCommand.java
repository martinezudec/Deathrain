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
import org.bukkit.scheduler.BukkitRunnable;

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
                case "start":
                    if (args.length == 2) {
                        try {
                            int duration = Integer.parseInt(args[1]);
                            startStorm(sender, duration);
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
        sender.sendMessage(MessageUtils.getColoredMessage("&6/deathrain start <duration> &f- &7Start the storm for a duration in minutes"));
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
        World overworld = Bukkit.getWorld(plugin.getMainConfigManager().getWorldName());
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

    public void startStorm(CommandSender sender, int duration) {
        World overworld = Bukkit.getWorld(plugin.getMainConfigManager().getWorldName());
        if (overworld == null) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cOverworld not found. Make sure the main world name is correct."));
            return;
        }

        long durationTicks = duration * 60L * 20L; // Convertir minutos a ticks

        if (!overworld.hasStorm()) {
            overworld.setStorm(true);
            PlayerListener.remainingTime = durationTicks;
        } else {
            PlayerListener.remainingTime += durationTicks;
        }

        if (PlayerListener.stormTask != null) {
            PlayerListener.stormTask.cancel();
        }

        PlayerListener.stormTask = new BukkitRunnable() {
            private long messageCooldown = 15; // En minutos

            @Override
            public void run() {
                if (PlayerListener.remainingTime <= 0) {
                    overworld.setStorm(false);
                    this.cancel();
                    Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                            "&7La tormenta ha terminado"));
                } else {
                    PlayerListener.remainingTime -= 60L * 20L;
                    messageCooldown--;

                    if (messageCooldown <= 0) {
                        long hours = PlayerListener.remainingTime / (3600L * 20L);
                        long minutes = (PlayerListener.remainingTime % (3600L * 20L)) / (60L * 20L);
                        Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                                "&7Tiempo restante de tormenta: &c" + hours + " horas y " + minutes + " minutos"));
                        messageCooldown = 15;
                    }
                }
            }
        };
        PlayerListener.stormTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("Deathrain"), 0L, 60L * 20L);

        sender.sendMessage(MessageUtils.getColoredMessage(
                Deathrain.prefix + "&aThe storm has started for " + duration + " minutes."));
    }
}
