package ahjd.asgardus.statserivce.api;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.mob.*;
import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.entity.Entity;

import java.util.HashMap;
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
     * Create mob with auto-generated UUID and behaviour
     */
    public static UUID createMob(Map<StatType, Integer> stats, Tier tier, MobType mobType, Behaviour behaviour, CombatType combatType) {
        return requirePlugin().getMobManager().createMob(stats, tier, mobType, behaviour, combatType);
    }

    /**
     * Create mob with specific UUID (e.g., entity UUID) and behaviour
     */
    public static void createMob(UUID uuid, Map<StatType, Integer> stats, Tier tier, MobType mobType, Behaviour behaviour, CombatType combatType) {
        requirePlugin().getMobManager().createMob(uuid, stats, tier, mobType, behaviour, combatType);
    }

    /**
     * Edit existing mob completely with behaviour
     */
    public static boolean editMob(UUID uuid, Map<StatType, Integer> newStats, Tier tier, MobType mobType, Behaviour behaviour, CombatType combatType) {
        return requirePlugin().getMobManager().editMob(uuid, newStats, tier, mobType, behaviour, combatType);
    }

    /**
     * Edit existing mob using entity with behaviour
     */
    public static boolean editMob(Entity entity, Map<StatType, Integer> newStats, Tier tier, MobType mobType, Behaviour behaviour, CombatType combatType) {
        return editMob(entity.getUniqueId(), newStats, tier, mobType, behaviour, combatType);
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
            mob.setStat(stat, Math.max(0, value)); // Ensure non-negative
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
            int newValue = Math.max(0, mob.getStat(stat) + value);
            mob.setStat(stat, newValue);
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

    // ===== NEW METHODS NEEDED BY GUI =====

    /**
     * Set all stats at once (needed by GUI)
     */
    public static boolean setStats(UUID uuid, Map<StatType, Integer> stats) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            // Validate all stats are non-negative
            for (Map.Entry<StatType, Integer> entry : stats.entrySet()) {
                int value = Math.max(0, entry.getValue());
                mob.setStat(entry.getKey(), value);
            }
            return true;
        }
        return false;
    }

    /**
     * Set all stats by entity
     */
    public static boolean setStats(Entity entity, Map<StatType, Integer> stats) {
        return setStats(entity.getUniqueId(), stats);
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
     * Get mob behaviour
     */
    public static Behaviour getBehaviour(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getBehaviour() : null;
    }

    /**
     * Get behaviour by entity
     */
    public static Behaviour getBehaviour(Entity entity) {
        return getBehaviour(entity.getUniqueId());
    }

    /**
     * Set mob behaviour
     */
    public static boolean setBehaviour(UUID uuid, Behaviour behaviour) {
        return requirePlugin().getMobManager().setBehaviour(uuid, behaviour);
    }

    /**
     * Set behaviour by entity
     */
    public static boolean setBehaviour(Entity entity, Behaviour behaviour) {
        return setBehaviour(entity.getUniqueId(), behaviour);
    }

    /**
     * Get mob combat type
     */
    public static CombatType getCombatType(UUID uuid) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        return mob != null ? mob.getCombatType() : null;
    }

    /**
     * Get combat type by entity
     */
    public static CombatType getCombatType(Entity entity) {
        return getCombatType(entity.getUniqueId());
    }

    /**
     * Set mob combat type
     */
    public static boolean setCombatType(UUID uuid, CombatType combatType) {
        return requirePlugin().getMobManager().setCombatType(uuid, combatType);
    }

    /**
     * Set mob combat type by entity
     */
    public static boolean setCombatType(Entity entity, CombatType combatType) {
        return setCombatType(entity.getUniqueId(), combatType);
    }

    // ===== QUERY METHODS =====

    /**
     * Utility methods for external plugins to query mobs
     */
    public static List<UUID> getAllMobsOfType(MobType mobType) {
        return requirePlugin().getMobManager().getAllMobsOfType(mobType);
    }

    public static List<UUID> getAllMobsOfTier(Tier tier) {
        return requirePlugin().getMobManager().getAllMobsOfTier(tier);
    }

    public static List<UUID> getAllMobsOfBehaviour(Behaviour behaviour) {
        return requirePlugin().getMobManager().getAllMobsOfBehaviour(behaviour);
    }

    public static List<UUID> getAllMobsOfCombatType(CombatType combatType) {
        return requirePlugin().getMobManager().getAllMobsOfCombatType(combatType);
    }

    public static List<UUID> getAllMobs() {
        return requirePlugin().getMobManager().getAllMobs();
    }

    // ===== TEMPORARY STAT MODIFICATIONS =====

    /**
     * Adds a named temporary stat boost to a mob.
     */
    public static void addTempStat(UUID uuid, StatType stat, int amount, String key, int durationSeconds) {
        MobData mob = requirePlugin().getMobManager().getMob(uuid);
        if (mob != null) {
            mob.addTempStat(stat, amount, durationSeconds, key, requirePlugin());
        }
    }

    public static void addTempStat(Entity entity, StatType stat, int amount, String key, int durationSeconds) {
        addTempStat(entity.getUniqueId(), stat, amount, key, durationSeconds);
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

    // ===== VALIDATION METHODS =====

    /**
     * Validate stat value is within acceptable range
     */
    public static int validateStatValue(StatType stat, int value) {
        // Ensure minimum value is 0
        value = Math.max(0, value);

        // Set reasonable maximum limits based on stat type
        return switch (stat) {
            case HEALTH, CURRENT_HEALTH -> Math.min(value, 100000);
            case DAMAGE -> Math.min(value, 10000);
            case HEALTH_REGENERATION -> Math.min(value, 1000);
            case VITALITY -> Math.min(value, 10000);
            case KNOCKBACK -> Math.min(value, 100);
            case KNOCKBACK_RESISTANCE -> Math.min(value, 100);
            case FALL_DAMAGE_RESISTANCE -> Math.min(value, 100);
            case FIRE_RESISTANCE -> Math.min(value, 100);
            default -> value;
        };
    }

    /**
     * Bulk validate stats map
     */
    public static Map<StatType, Integer> validateStats(Map<StatType, Integer> stats) {
        Map<StatType, Integer> validatedStats = new HashMap<>();
        for (Map.Entry<StatType, Integer> entry : stats.entrySet()) {
            validatedStats.put(entry.getKey(), validateStatValue(entry.getKey(), entry.getValue()));
        }
        return validatedStats;
    }
}

/* ===== USAGE EXAMPLES =====

// 1. CREATING MOBS
Map<StatType, Integer> eliteStats = Map.of(
    StatType.HEALTH, 150,
    StatType.DAMAGE, 35,
    StatType.KNOCKBACK_RESISTANCE, 75
);

// Create with auto-generated UUID
UUID skeletonUUID = AsgMobStatsAPI.createMob(eliteStats, Tier.ELITE, MobType.SKELETON, Behaviour.AGGRESSIVE, CombatType.RANGED);

// Create using existing entity
Entity dragonEntity = world.spawnEntity(location, EntityType.ENDER_DRAGON);
AsgMobStatsAPI.createMob(dragonEntity.getUniqueId(), eliteStats, Tier.BOSS, MobType.DRAGON, Behaviour.PROTECTIVE, CombatType.HYBRID);

// 2. RETRIEVING & MODIFYING MOBS
MobData skeleton = AsgMobStatsAPI.getMob(skeletonUUID);
if (skeleton != null) {
    // Get current stats
    int health = skeleton.getStat(StatType.HEALTH);
    Behaviour currentBehaviour = skeleton.getBehaviour();
    CombatType currentCombatType = skeleton.getCombatType();

    // Modify individual properties
    AsgMobStatsAPI.setStat(skeletonUUID, StatType.DAMAGE, 50);
    AsgMobStatsAPI.setBehaviour(skeletonUUID, Behaviour.NEUTRAL);
    AsgMobStatsAPI.setCombatType(skeletonUUID, CombatType.MELEE);
    AsgMobStatsAPI.setTier(skeletonUUID, Tier.LEGENDARY);
}

// 3. TEMPORARY STAT MODIFICATIONS )
// Add temporary damage boost for 30 seconds
AsgMobStatsAPI.addTempStat(skeletonUUID, StatType.DAMAGE, 25, "rage_boost", 30);

// Add 50% health boost for 60 seconds
AsgMobStatsAPI.addPercentageBoost(skeletonUUID, StatType.HEALTH, 50, "healing_aura", 60);

// 4. BULK STAT UPDATES (NEW)
Map<StatType, Integer> newStats = Map.of(
    StatType.HEALTH, 200,
    StatType.DAMAGE, 45,
    StatType.KNOCKBACK_RESISTANCE, 80
);
AsgMobStatsAPI.setStats(skeletonUUID, newStats);

// 5. VALIDATION (NEW)
int validatedHealth = AsgMobStatsAPI.validateStatValue(StatType.HEALTH, -50); // Returns 0
Map<StatType, Integer> validatedStats = AsgMobStatsAPI.validateStats(eliteStats);
 */