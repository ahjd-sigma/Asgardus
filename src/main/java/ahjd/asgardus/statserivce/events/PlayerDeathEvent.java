package ahjd.asgardus.statserivce.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class PlayerDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private Location respawnLocation;
    private boolean keepInventory;
    private int experienceLossPercentage;

    public PlayerDeathEvent(Player player) {
        World world = player.getLocation().getWorld();
        this.player = player;
        if (world != null) {
            this.respawnLocation = world.getSpawnLocation();
        }else{
            this.respawnLocation = player.getRespawnLocation();
        }
        this.keepInventory = true;
        this.experienceLossPercentage = 10; // Default 10% XP loss
    }

    public Player getPlayer() {
        return player;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }

    public boolean shouldKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public int getExperienceLossPercentage() {
        return experienceLossPercentage;
    }

    public void setExperienceLossPercentage(int experienceLossPercentage) {
        this.experienceLossPercentage = Math.min(100, Math.max(0, experienceLossPercentage));
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}