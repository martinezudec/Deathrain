package dr.mz27.commands;

import dr.mz27.Deathrain;
import dr.mz27.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.getColoredMessage(
                    Deathrain.prefix + "&cYou must be in game to use this command"));
            return true;
        }

        if (args.length >= 1) {
            switch (args[0]) {
                case "help":
                    sender.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&d&l<---------------------Commands--------------------->"));
                    sender.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&6/deathrain help &f- &7Shows this help message"));
                    sender.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&d&l<---------------------Commands--------------------->"));
                    break;
                default:
                    sender.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&6Type '/deathrain help' for help"));
            }
        } else {
            sender.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&6Type '/deathrain help' for help"));
        }

        return true;
    }
}
