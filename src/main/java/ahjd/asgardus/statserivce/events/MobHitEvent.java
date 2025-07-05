package ahjd.asgardus.statserivce.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MobHitEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity mob;
    private final Player attacker;
    private boolean cancelled;

    public MobHitEvent(LivingEntity mob, Player attacker) {
        this.mob = mob;
        this.attacker = attacker;
    }

    public LivingEntity getMob() {
        return mob;
    }

    public Player getAttacker() {
        return attacker;
    }

    @Override
    public HandlerList getHandlers() {
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

/*
========= FIRE THE EVENT ========

if (entity instanceof LivingEntity living && damager instanceof Player player) {
    if (AsgMobStatsAPI.hasMob(living)) {
        Bukkit.getPluginManager().callEvent(new MobHitEvent(living, player));
    }
}

====== LISTEN FOR THE EVENT ========

@EventHandler
public void onMobHit(MobHitEvent event) {
    LivingEntity mob = event.getMob();
    Player attacker = event.getAttacker();

    // You can store this in metadata, MobData, etc.
    mob.setMetadata("lastHitTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));
    mob.setMetadata("lastHitBy", new FixedMetadataValue(plugin, attacker.getUniqueId().toString()));
}
 */