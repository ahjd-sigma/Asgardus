package ahjd.asgardus.statserivce.api;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatSettings;
import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class AsgStatsAPI {

    private static Asgardus plugin;

    // Called once on plugin enable
    public static void init(Asgardus instance) {
        plugin = instance;
    }

    public static int getStat(UUID uuid, StatType stat) {
        return plugin.getPlayerManager().getOrCreate(uuid).getStat(stat);
    }

    public static void setStat(UUID uuid, StatType stat, int value) {
        plugin.getPlayerManager().getOrCreate(uuid).setStat(stat, value, plugin);
    }

    public static void addStat(UUID uuid, StatType stat, int value) {
        plugin.getPlayerManager().getOrCreate(uuid).addStat(stat, value, plugin);
    }

    public static void resetStat(UUID uuid, StatType stat) {
        int def = plugin.getStatSettings(stat).getDefaultValue();
        setStat(uuid, stat, def);
    }

    public static void resetAllStats(UUID uuid) {
        for (StatType stat : StatType.values()) {
            resetStat(uuid, stat);
        }
    }

    public static Map<StatType, Integer> getAllStats(UUID uuid) {
        return plugin.getPlayerManager().getOrCreate(uuid).getAllStats();
    }

    /**
     * Clears all active temporary stat boosts for a player (duration-based boosts).
     * @param uuid The player's UUID.
     */
    public static void clearAllTempStats(UUID uuid) {
        plugin.getPlayerManager().getOrCreate(uuid).clearAllTempStats(plugin);
    }

    /**
     * Adds a temporary stat increase for a specified duration (in seconds).
     * @param uuid The player's UUID.
     * @param stat The stat to modify.
     * @param amount The amount to add.
     * @param durationSeconds The duration in seconds for the temporary increase.
     */
    public static void addTempStat(UUID uuid, StatType stat, int amount, int durationSeconds) {
        plugin.getPlayerManager().getOrCreate(uuid).addTempStat(stat, amount, durationSeconds, plugin);
    }

    /**
     * Adds a temporary stat increase for a specified duration (in seconds).
     * @param player The Player object.
     * @param stat The stat to modify.
     * @param amount The amount to add.
     * @param durationSeconds The duration in seconds for the temporary increase.
     */
    public static void addTempStat(Player player, StatType stat, int amount, int durationSeconds) {
        addTempStat(player.getUniqueId(), stat, amount, durationSeconds);
    }

    /**
     * Adds a percentage-based increase to a stat (e.g., +150% of current value).
     * @param uuid The player's UUID.
     * @param stat The stat to modify.
     * @param percent The percent to add (e.g., 150 for +150%).
     */
    public static void addStatPercent(UUID uuid, StatType stat, int percent) {
        plugin.getPlayerManager().getOrCreate(uuid).addStatPercent(stat, percent, plugin);
    }

    /**
     * Adds a percentage-based increase to a stat (e.g., +150% of current value).
     * @param player The Player object.
     * @param stat The stat to modify.
     * @param percent The percent to add (e.g., 150 for +150%).
     */
    public static void addStatPercent(Player player, StatType stat, int percent) {
        addStatPercent(player.getUniqueId(), stat, percent);
    }
}