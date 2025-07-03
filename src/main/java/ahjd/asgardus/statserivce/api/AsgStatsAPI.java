package ahjd.asgardus.statserivce.api;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatSettings;
import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.entity.Player;

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
    public static void addTempStat(UUID uuid, StatType stat, int amount, int durationSeconds, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addTempStat(stat, amount, durationSeconds, key, requirePlugin());
    }
    public static void addTempStat(Player player, StatType stat, int amount, int durationSeconds, String key) {
        addTempStat(player.getUniqueId(), stat, amount, durationSeconds, key);
    }
    public static void removeTempStat(UUID uuid, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).removeTempStat(key);
    }
    public static void removeTempStat(Player player, String key) {
        removeTempStat(player.getUniqueId(), key);
    }
    /**
     * Adds a named percentage stat boost to a player.
     */
    public static void addPercentageBoost(UUID uuid, StatType stat, int percent, String key, int durationSeconds) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addPercentageBoost(stat, percent, key, durationSeconds, requirePlugin());
    }
    public static void addPercentageBoost(Player player, StatType stat, int percent, String key, int durationSeconds) {
        addPercentageBoost(player.getUniqueId(), stat, percent, key, durationSeconds);
    }
    public static void removePercentageBoost(UUID uuid, String key) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).removePercentageBoost(key, requirePlugin());
    }
    public static void removePercentageBoost(Player player, String key) {
        removePercentageBoost(player.getUniqueId(), key);
    }
    /**
     * Clears all temporary and percentage boosts for a player.
     */
    public static void clearAllTempStatsAndBoosts(UUID uuid) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).clearAllTempStatsAndBoosts(requirePlugin());
    }
    public static void clearAllTempStatsAndBoosts(Player player) {
        clearAllTempStatsAndBoosts(player.getUniqueId());
    }

    public static void addStatPercent(UUID uuid, StatType stat, int percent) {
        requirePlugin().getPlayerManager().getOrCreate(uuid).addStatPercent(stat, percent, requirePlugin());
    }

    public static void addStatPercent(Player player, StatType stat, int percent) {
        addStatPercent(player.getUniqueId(), stat, percent);
    }
}