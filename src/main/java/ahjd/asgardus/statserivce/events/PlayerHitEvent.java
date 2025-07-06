package ahjd.asgardus.statserivce.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class PlayerHitEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity attacker;
    private final Player player;
    private boolean cancelled;

    public PlayerHitEvent(Player player, LivingEntity attacker) {
        this.player = player;
        this.attacker = attacker;
    }

    public Player getPlayer() {
        return player;
    }

    public LivingEntity getAttacker() {
        return attacker;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}