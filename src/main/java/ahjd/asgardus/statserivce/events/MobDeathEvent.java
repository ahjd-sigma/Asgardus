package ahjd.asgardus.statserivce.events;

import ahjd.asgardus.statserivce.mob.MobData;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final UUID mobUUID;
    private final Entity entity;
    private final MobData mobData;
    private final List<ItemStack> drops = new ArrayList<>();
    private int experienceDropped;
    private boolean customDrops;

    public MobDeathEvent(UUID mobUUID, Entity entity, MobData mobData) {
        this.mobUUID = mobUUID;
        this.entity = entity;
        this.mobData = mobData;
        this.experienceDropped = 0;
        this.customDrops = false;
    }

    public UUID getMobUUID() {
        return mobUUID;
    }

    public Entity getEntity() {
        return entity;
    }

    public MobData getMobData() {
        return mobData;
    }

    public List<ItemStack> getDrops() {
        return new ArrayList<>(drops);
    }

    public void addDrop(ItemStack item) {
        drops.add(item);
        customDrops = true;
    }

    public void clearDrops() {
        drops.clear();
        customDrops = true;
    }

    public int getExperienceDropped() {
        return experienceDropped;
    }

    public void setExperienceDropped(int experienceDropped) {
        this.experienceDropped = Math.max(0, experienceDropped);
    }

    public boolean hasCustomDrops() {
        return customDrops;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}