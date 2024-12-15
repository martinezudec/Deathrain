package dr.mz27.listeners;

import dr.mz27.Deathrain;
import dr.mz27.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

public class PlayerListener implements Listener {

    private static long stormDuration = 3600L * 20L;
    public static long remainingTime = 0L;
    public static BukkitRunnable stormTask;
    private final Deathrain plugin;

    public PlayerListener(Deathrain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage(MessageUtils.getColoredMessage("&c" + player.getName() + " &7died"));
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

        //Inicio de la Deathrain
        World overworld = Bukkit.getWorld(plugin.getMainConfigManager().getWorldName());
        if (overworld == null) {
            Bukkit.getLogger().warning("Overworld no encontrado. Asegúrate de que el nombre del mundo principal sea correcto.");
            return;
        }

        if (!overworld.hasStorm()) {
            overworld.setStorm(true);
            remainingTime = stormDuration;
        } else {
            remainingTime += stormDuration;
        }


        //Cancelar y reprogramar la tarea
        if (stormTask != null) {
            stormTask.cancel();
        }

        stormTask = new BukkitRunnable() {
            private long messageCooldown = 15; // En minutos

            @Override
            public void run() {
                if (remainingTime <= 0) {
                    overworld.setStorm(false);
                    this.cancel();
                    Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                            "&7La tormenta ha terminado"));
                } else {
                    // Reducir tiempo restante
                    remainingTime -= 60L * 20L;
                    messageCooldown--;

                    // Mensaje de tiempo restante
                    if (messageCooldown <= 0) {
                        long hours = remainingTime / (3600L * 20L);
                        long minutes = (remainingTime % (3600L * 20L)) / (60L * 20L);
                        Bukkit.broadcastMessage(MessageUtils.getColoredMessage(
                                "&7Tiempo restante de tormenta: &c" + hours + " horas y " + minutes + " minutos"));
                        messageCooldown = 15;
                    }
                }
            }
        };
        stormTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("Deathrain"), 0L, 60L * 20L);
    }
}
