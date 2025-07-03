package ahjd.asgardus.statserivce.api;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatType;
import ahjd.asgardus.statserivce.mob.MobData;
import ahjd.asgardus.statserivce.mob.MobType;
import ahjd.asgardus.statserivce.mob.Tier;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.UUID;
import java.util.List;

public class AsgMobStatsAPI {

    private static Asgardus plugin;

    /**
     * Called once on plugin enable. Must be called before any API usage.
     */
    public static void init(Asgardus instance) {
        plugin = instance;
    }

    private static Asgardus requirePlugin() {
        if (plugin == null) {
            throw new IllegalStateException("AsgMobStatsAPI.plugin is not initialized! Make sure AsgMobStatsAPI.init() is called in onEnable().");
        }
        return plugin;
    }

    /**
     * Create mob with auto-generated UUID
     */
    public static UUID createMob(Map<StatType, Integer> stats, Tier tier, MobType mobType) {
        return requirePlugin().getMobManager().createMob(stats, tier, mobType);
    }

    /**
     * Create mob with specific UUID (e.g., entity UUID)
     */
    public static void createMob(UUID uuid, Map<StatType, Integer> stats, Tier tier, MobType mobType) {
        requirePlugin().getMobManager().createMob(uuid, stats, tier, mobType);
    }

    /**
     * Create mob using entity UUID
     */
    public static void createMob(Entity entity, Map<StatType, Integer> stats, Tier tier, MobType mobType) {
        createMob(entity.getUniqueId(), stats, tier, mobType);
    }

    /**
     * Edit existing mob completely
     */
    public static boolean editMob(UUID uuid, Map<StatType, Integer> newStats, Tier tier, MobType mobType) {
        return requirePlugin().getMobManager().editMob(uuid, newStats, tier, mobType);
    }

    /**
     * Edit existing mob using entity
     */
    public static boolean editMob(Entity entity, Map<StatType, Integer> newStats, Tier tier, MobType mobType) {
        return editMob(entity.getUniqueId(), newStats, tier, mobType);
    }

    /**
     * Get mob data by UUID
     */
    public static MobData getMob(UUID uuid) {
        return requirePlugin().getMobManager().getMob(uuid);
    }

    /**
     * Get mob data by entity
     */
    public static MobData getMob(Entity entity) {
        return getMob(entity.getUniqueId());
    }

    /**
     * Check if mob exists
     */
    public static boolean hasMob(UUID uuid) {
        return requirePlugin().getMobManager().hasMob(uuid);
    }

    /**
     * Check if mob exists by entity
     */
    public static boolean hasMob(Entity entity) {
        return hasMob(entity.getUniqueId());
    }

    /**
     * Remove mob from memory
     */
    public static boolean removeMob(UUID uuid) {
        return requirePlugin().getMobManager().removeMob(uuid);
    }

    /**
     * Remove mob by entity
     */
    public static boolean removeMob(Entity entity) {
        return removeMob(entity.getUniqueId());
    }

    /**
     * Get specific stat
     */
    public static int getStat(UUID uuid, StatType stat) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getStat(stat) : 0;
    }

    /**
     * Get stat by entity
     */
    public static int getStat(Entity entity, StatType stat) {
        return getStat(entity.getUniqueId(), stat);
    }

    /**
     * Set specific stat
     */
    public static void setStat(UUID uuid, StatType stat, int value) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.setStat(stat, value);
        }
    }

    /**
     * Set stat by entity
     */
    public static void setStat(Entity entity, StatType stat, int value) {
        setStat(entity.getUniqueId(), stat, value);
    }

    /**
     * Add to stat
     */
    public static void addStat(UUID uuid, StatType stat, int value) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.setStat(stat, mob.getStat(stat) + value);
        }
    }

    /**
     * Add to stat by entity
     */
    public static void addStat(Entity entity, StatType stat, int value) {
        addStat(entity.getUniqueId(), stat, value);
    }

    /**
     * Get all stats
     */
    public static Map<StatType, Integer> getAllStats(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getStats() : Map.of();
    }

    /**
     * Get all stats by entity
     */
    public static Map<StatType, Integer> getAllStats(Entity entity) {
        return getAllStats(entity.getUniqueId());
    }

    /**
     * Get mob tier
     */
    public static Tier getTier(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getTier() : null;
    }

    /**
     * Get tier by entity
     */
    public static Tier getTier(Entity entity) {
        return getTier(entity.getUniqueId());
    }

    /**
     * Set mob tier
     */
    public static void setTier(UUID uuid, Tier tier) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.setTier(tier);
        }
    }

    /**
     * Set tier by entity
     */
    public static void setTier(Entity entity, Tier tier) {
        setTier(entity.getUniqueId(), tier);
    }

    /**
     * Get mob type
     */
    public static MobType getMobType(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getMobType() : null;
    }

    /**
     * Get mob type by entity
     */
    public static MobType getMobType(Entity entity) {
        return getMobType(entity.getUniqueId());
    }

    /**
     * Set mob type
     */
    public static void setMobType(UUID uuid, MobType mobType) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.setMobType(mobType);
        }
    }

    /**
     * Set mob type by entity
     */
    public static void setMobType(Entity entity, MobType mobType) {
        setMobType(entity.getUniqueId(), mobType);
    }

    /**
     * Utility methods for external plugins
     */
    public static List<UUID> getAllMobsOfType(MobType mobType) {
        return requirePlugin().getMobManager().getAllMobsOfType(mobType);
    }

    public static List<UUID> getAllMobsOfTier(Tier tier) {
        return requirePlugin().getMobManager().getAllMobsOfTier(tier);
    }

    public static List<UUID> getAllMobs() {
        return requirePlugin().getMobManager().getAllMobs();
    }

/* eggsample
// Create mob with auto-generated UUID
Map<StatType, Integer> stats = Map.of(
    StatType.HEALTH, 100,
    StatType.DAMAGE, 25,
    StatType.KNOCKBACK_RESISTANCE, 50
);
UUID mobUUID = AsgMobStatsAPI.createMob(stats, Tier.ELITE, MobType.SKELETON);

// Or use entity's UUID
Entity zombie = world.spawnEntity(location, EntityType.ZOMBIE);
AsgMobStatsAPI.createMob(zombie.getUniqueId(), stats, Tier.BOSS, MobType.DRAGON);

// Edit mob later
Map<StatType, Integer> newStats = Map.of(
    StatType.HEALTH, 200,
    StatType.DAMAGE, 40
);
AsgMobStatsAPI.editMob(mobUUID, newStats, Tier.LEGENDARY, MobType.FIRE_ELEMENTAL);

// Retrieve mob
MobData mob = AsgMobStatsAPI.getMob(mobUUID);
if (mob != null) {
    System.out.println("Health: " + mob.getStat(StatType.HEALTH));
    System.out.println("Type: " + mob.getMobType());
    System.out.println("Tier: " + mob.getTier());
}

// Quick access
int health = AsgMobStatsAPI.getStat(mobUUID, StatType.HEALTH);
AsgMobStatsAPI.setStat(mobUUID, StatType.DAMAGE, 30);
AsgMobStatsAPI.setTier(mobUUID, Tier.MYTHIC);

// External plugins can do whatever they want
List<UUID> allSkeletons = AsgMobStatsAPI.getAllMobsOfType(MobType.SKELETON);
List<UUID> allBosses = AsgMobStatsAPI.getAllMobsOfTier(Tier.BOSS);

// External plugin example: "Deal 2x damage to all SKELETON type mobs"
for (UUID uuid : AsgMobStatsAPI.getAllMobsOfType(MobType.SKELETON)) {
    // Apply damage multiplier logic
}
 */

    /**
     * Adds a named temporary stat boost to a mob.
     */
    public static void addTempStat(UUID uuid, StatType stat, int amount, int durationSeconds, String key) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.addTempStat(stat, amount, durationSeconds, key, requirePlugin());
        }
    }
    public static void addTempStat(Entity entity, StatType stat, int amount, int durationSeconds, String key) {
        addTempStat(entity.getUniqueId(), stat, amount, durationSeconds, key);
    }
    public static void removeTempStat(UUID uuid, String key) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.removeTempStat(key);
        }
    }
    public static void removeTempStat(Entity entity, String key) {
        removeTempStat(entity.getUniqueId(), key);
    }
    /**
     * Adds a named percentage stat boost to a mob.
     */
    public static void addPercentageBoost(UUID uuid, StatType stat, int percent, String key, int durationSeconds) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.addPercentageBoost(stat, percent, key, durationSeconds, requirePlugin());
        }
    }
    public static void addPercentageBoost(Entity entity, StatType stat, int percent, String key, int durationSeconds) {
        addPercentageBoost(entity.getUniqueId(), stat, percent, key, durationSeconds);
    }
    public static void removePercentageBoost(UUID uuid, String key) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.removePercentageBoost(key);
        }
    }
    public static void removePercentageBoost(Entity entity, String key) {
        removePercentageBoost(entity.getUniqueId(), key);
    }
    /**
     * Clears all temporary and percentage boosts for a mob.
     */
    public static void clearAllTempStatsAndBoosts(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.clearAllTempStatsAndBoosts();
        }
    }
    public static void clearAllTempStatsAndBoosts(Entity entity) {
        clearAllTempStatsAndBoosts(entity.getUniqueId());
    }
}