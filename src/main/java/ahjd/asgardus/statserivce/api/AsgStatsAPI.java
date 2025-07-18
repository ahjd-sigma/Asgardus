package ahjd.asgardus.statserivce.api;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatSettings;
import ahjd.asgardus.statserivce.utils.StatType;

import java.util.Map;
import java.util.UUID;

public class AsgStatsAPI {

    private static Asgardus plugin;

    /**
     * Called once on plugin enable. Must be called before any API usage.
     */
    public static void init(Asgardus instance) {
        plugin = instance;
    }

    private static Asgardus requirePlugin() {
        if (plugin == null) {
            throw new IllegalStateException("AsgStatsAPI.plugin is not initialized! Make sure AsgStatsAPI.init() is called in onEnable().");
        }
        return plugin;
    }

    public static int getStat(UUID uuid, StatType stat) {
        return requirePlugin().getPlayerManager().getOrCreate(uuid).getStat(stat);
    }

    public static void setStat(UUID uuid, StatType stat, int value) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).setStat(stat, value, requirePlugin());
    }

    public static void addStat(UUID uuid, StatType stat, int value) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addStat(stat, value, requirePlugin());
    }

    public static void resetStat(UUID uuid, StatType stat) {
        int def = requirePlugin().getStatSettings(stat).getDefaultValue();
        setStat(uuid, stat, def);
    }

    public static void resetAllStats(UUID uuid) {
        for (StatType stat : StatType.values()) {
            resetStat(uuid, stat);
        }
    }

    public static Map<StatType, Integer> getAllStats(UUID uuid) {
        return requirePlugin().getPlayerManager().getOrCreate(uuid).getAllStats();
    }

    public static void clearAllTempStats(UUID uuid) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).clearAllTempStats(requirePlugin());
    }

    /**
     * Adds a named temporary stat boost to a player.
     */
    /**
     * Adds a named temporary stat boost to a player (by UUID).
     * @param uuid Player UUID
     * @param stat StatType
     * @param amount Flat amount to add
     * @param durationSeconds Duration in seconds (0 for permanent)
     * @param key Unique key for this boost
     */
    public static void addTempStat(UUID uuid, StatType stat, int amount, int durationSeconds, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addTempStat(stat, amount, durationSeconds, key, requirePlugin());
    }

    /**
     * Removes a temporary stat boost by key (by UUID).
     * @param uuid Player UUID
     * @param key Unique key
     */
    public static void removeTempStat(UUID uuid, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).removeTempStat(key);
    }
    /**
     * Adds a named percentage stat boost to a player.
     */
    /**
     * Adds a percentage boost to a stat (by UUID), with a key for easy removal.
     * Calculation order: (base + all flat boosts) → then apply all percent boosts.
     * For example: if base=10, flat+5, percent+10% → (10+5)=15, then 15*1.1=16.5
     * @param uuid Player UUID
     * @param stat StatType
     * @param percent Percent boost (e.g. 10 for +10%)
     * @param key Unique key for this boost
     * @param durationSeconds Duration in seconds (0 for permanent)
     */
    public static void addPercentageBoost(UUID uuid, StatType stat, int percent, String key, int durationSeconds) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addPercentageBoost(stat, percent, key, durationSeconds, requirePlugin());
    }

    /**
     * Removes a percentage boost by key (by UUID).
     * @param uuid Player UUID
     * @param key Unique key
     */
    public static void removePercentageBoost(UUID uuid, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).removePercentageBoost(key, requirePlugin());
    }
    /**
     * Clears all temporary and percentage boosts for a player.
     */
    /**
     * Clears all temporary and percentage boosts for a player (by UUID).
     * @param uuid Player UUID
     */
    public static void clearAllTempStatsAndBoosts(UUID uuid) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).clearAllTempStatsAndBoosts(requirePlugin());
    }
}