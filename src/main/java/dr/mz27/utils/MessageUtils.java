package dr.mz27.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MessageUtils {
    public static String getColoredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // Comando para los tiempos
        String command = String.format("title @a times %d %d %d", fadeIn, stay, fadeOut);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        // Subtítulo (convertir a JSON)
        if (subtitle != null && !subtitle.isEmpty()) {
            String subtitleJson = String.format("{\"text\":\"%s\"}", subtitle.replace("&", "§"));
            command = String.format("title @a subtitle %s", subtitleJson);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        // Título principal (convertir a JSON)
        if (title != null && !title.isEmpty()) {
            String titleJson = String.format("{\"text\":\"%s\"}", title.replace("&", "§"));
            command = String.format("title @a title %s", titleJson);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

}
