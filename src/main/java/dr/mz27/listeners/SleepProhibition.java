package dr.mz27.listeners;

import dr.mz27.Deathrain;
import dr.mz27.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;



public class SleepProhibition implements Listener {
    private final Deathrain plugin;

    public SleepProhibition(Deathrain plugin) {
        this.plugin = plugin;
    }

    public void prohibitSleep() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void allowSleep() {
        PlayerBedEnterEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(MessageUtils.getColoredMessage(Deathrain.prefix + "&4Algun &b&kaweonao&4 se murió, así que no se puede dormir"));
        event.setCancelled(true);
    }
}
