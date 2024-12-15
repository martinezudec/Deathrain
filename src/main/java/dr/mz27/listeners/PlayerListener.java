package dr.mz27.listeners;

import dr.mz27.Deathrain;
import dr.mz27.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

public class PlayerListener implements Listener {

    public static long remainingTime = 0L;
    public static BukkitRunnable stormTask;
    private final Deathrain plugin;
    private static BossBar bossBar;
    private static long totalDurationTicks;

    public PlayerListener(Deathrain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage(MessageUtils.getColoredMessage("&c" + player.getName() + " &7died"));

        MessageUtils.sendTitleToAll("&c" + player.getName() + " &7died", "&7Deathrain is coming", 10, 60, 20);
        player.getWorld().strikeLightningEffect(player.getLocation());

        //Ubicacion del jugador
        Block graveBlock = player.getLocation().getBlock();

        //Lapida
        graveBlock.setType(Material.CHISELED_TUFF_BRICKS);

        //Cabeza del jugador en la muralla
        Block headBlock = graveBlock.getRelative(0, 1, 0);
        headBlock.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) headBlock.getState();
        skull.setOwningPlayer(player);
        skull.update();

        startStorm(3600L * 20L); // 1 hora en ticks
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (bossBar != null && bossBar.isVisible()) {
            bossBar.addPlayer(event.getPlayer());
        }
    }

    public void startStorm(long durationTicks) {
        World overworld = Bukkit.getWorld(plugin.getMainConfigManager().getWorldName());
        if (overworld == null) {
            Bukkit.getLogger().warning("Overworld no encontrado. Asegúrate de que el nombre del mundo principal sea correcto.");
            return;
        }

        // Cancelar la tarea anterior y ocultar la BossBar antigua
        if (stormTask != null) {
            stormTask.cancel();
            stormTask = null;
        }
        if (bossBar != null) {
            bossBar.setVisible(false);
            bossBar.removeAll();
            bossBar = null;
        }

        if (!overworld.hasStorm()) {
            overworld.setStorm(true);
            remainingTime = durationTicks;
            totalDurationTicks = durationTicks;
            overworld.setWeatherDuration((int) remainingTime);
            long hours = remainingTime / (3600L * 20L);
            long minutes = (remainingTime % (3600L * 20L)) / (60L * 20L);
            Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                    "&dLa tormenta ha comenzado"));
            Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                    "&7Tiempo restante de tormenta: &c" + hours + " horas y " + minutes + " minutos"));
        } else {
            remainingTime += durationTicks;
            totalDurationTicks += durationTicks;
            overworld.setWeatherDuration((int) remainingTime);
            long hours = remainingTime / (3600L * 20L);
            long minutes = (remainingTime % (3600L * 20L)) / (60L * 20L);
            Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                    "&dLas muertes alimentan a la tormenta"));
            Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                    "&7Tiempo restante de tormenta: &c" + hours + " horas y " + minutes + " minutos"));
        }

        stormTask = new BukkitRunnable() {
            private long messageCooldown = 15 * 60;

            @Override
            public void run() {
                if (bossBar == null) {
                    bossBar = Bukkit.createBossBar("Deathrain Time Remaining", BarColor.RED, BarStyle.SOLID);
                    bossBar.setVisible(true);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        bossBar.addPlayer(onlinePlayer);
                    }
                }

                if (remainingTime <= 0) {
                    overworld.setStorm(false);
                    bossBar.setVisible(false);
                    this.cancel();
                    Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                            "&7La tormenta ha terminado"));
                } else {
                    // Check if there are any players online
                    if (Bukkit.getOnlinePlayers().isEmpty()) {
                        return;
                    }

                    // Reducir tiempo restante
                    remainingTime -= 2 * 20L;
                    messageCooldown -= 2;

                    double progress = (double) remainingTime / totalDurationTicks;
                    bossBar.setProgress(progress);
                    overworld.setWeatherDuration((int) remainingTime);

                    // Mensaje de tiempo restante
                    if (messageCooldown <= 0) {
                        long hours = remainingTime / (3600L * 20L);
                        long minutes = (remainingTime % (3600L * 20L)) / (60L * 20L);
                        Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                                "&7Tiempo restante de tormenta: &c" + hours + " horas y " + minutes + " minutos"));
                        messageCooldown = 15 * 60;
                    }
                }
            }
        };
        stormTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("Deathrain"), 0L, 2L * 20L);
    }

    public static void stopStorm(Deathrain plugin) {
        World overworld = Bukkit.getWorld(plugin.getMainConfigManager().getWorldName());
        if (overworld == null) {
            Bukkit.getLogger().warning("Overworld not found. Make sure the main world name is correct.");
            return;
        }

        if (stormTask != null) {
            stormTask.cancel();
            stormTask = null;
        }

        if (bossBar != null) {
            bossBar.setVisible(false);
            bossBar.removeAll();
            bossBar = null;
        }

        overworld.setStorm(false);
        remainingTime = 0L;
        totalDurationTicks = 0L;
    }
}