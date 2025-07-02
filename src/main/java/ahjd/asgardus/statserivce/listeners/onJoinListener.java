package ahjd.asgardus.statserivce.listeners;

import ahjd.asgardus.Asgardus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class onJoinListener implements Listener {

    private final Asgardus plugin;

    public onJoinListener(Asgardus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        // Create and track player data with defaults
        plugin.getPlayerManager().getOrCreate(uuid);

        // Optional: debug message
        plugin.getLogger().info("Started tracking stats for " + e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        // Stop tracking player data (remove from memory)
        plugin.getPlayerManager().remove(uuid);

        // Optional: debug message
        plugin.getLogger().info("Stopped tracking stats for " + e.getPlayer().getName());
    }
}
