package ahjd.asgardus.statserivce;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatType;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;


public class PlayerData {
    private final UUID uuid;
    private final Map<StatType, Integer> stats = new EnumMap<>(StatType.class);

    public PlayerData(UUID uuid, Asgardus plugin) {
        this.uuid = uuid;

        for (StatType stat : StatType.values()) {
            int def = plugin.getStatSettings(stat).getDefaultValue();
            stats.put(stat, def);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getStat(StatType stat) {
        synchronized (stats) {
            return stats.getOrDefault(stat, 0);
        }
    }

    public void setStat(StatType stat, int amount, Asgardus plugin) {
        int clamped = plugin.getStatSettings(stat).clamp(amount);
        synchronized (stats) {
            stats.put(stat, clamped);
        }
    }

    public void addStat(StatType stat, int amount, Asgardus plugin) {
        synchronized (stats) {
            int current = getStat(stat);
            int clamped = plugin.getStatSettings(stat).clamp(current + amount);
            stats.put(stat, clamped);
        }
    }

    /**
     * Resets all stats to their default values.
     * @param plugin The plugin instance for default stat values.
     */
    public void resetStats(Asgardus plugin) {
        synchronized (stats) {
            for (StatType stat : StatType.values()) {
                int def = plugin.getStatSettings(stat).getDefaultValue();
                stats.put(stat, def);
            }
        }
    }

    public Map<StatType, Integer> getAllStats() {
        synchronized (stats) {
            return new EnumMap<>(stats);
        }
    }

    /**
     * Adds a temporary stat increase for a specified duration (in seconds).
     * @param stat The stat to modify.
     * @param amount The amount to add.
     * @param durationSeconds The duration in seconds for the temporary increase.
     * @param plugin The plugin instance (must provide a scheduler).
     */
    public void addTempStat(StatType stat, int amount, int durationSeconds, String key, Asgardus plugin) {
        addStat(stat, amount, plugin);
        Runnable remover = () -> addStat(stat, -amount, plugin);
        tempBoostRemovers.put(key, remover);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            remover.run();
            tempBoostRemovers.remove(key);
        }, durationSeconds * 20L);
    }

    public void removeTempStat(String key) {
        Runnable remover = tempBoostRemovers.remove(key);
        if (remover != null) remover.run();
    }

    /**
     * Adds a percentage-based increase to a stat (e.g., +150% of current value).
     * @param stat The stat to modify.
     * @param percent The percent to add (e.g., 150 for +150%).
     * @param plugin The plugin instance for clamping.
     */
    public void addStatPercent(StatType stat, int percent, Asgardus plugin) {
        synchronized (stats) {
            int current = getStat(stat);
            int delta = (current * percent) / 100;
            int clamped = plugin.getStatSettings(stat).clamp(current + delta);
            stats.put(stat, clamped);
        }
    }

    /**
     * Clears all active temporary stat boosts immediately.
     * @param plugin The plugin instance for stat clamping.
     */
    public void clearAllTempStats(Asgardus plugin) {
        for (Runnable remover : tempBoostRemovers.values()) {
            remover.run();
        }
        tempBoostRemovers.clear();
    }

    // Track active temp boosts for clearing
    // Remove: private final Set<Runnable> tempBoostRemovers = ConcurrentHashMap.newKeySet();
    private final Map<String, Runnable> tempBoostRemovers = new ConcurrentHashMap<>();
    private final Map<String, PercentageBoost> percentageBoosts = new ConcurrentHashMap<>();

    public void addPercentageBoost(StatType stat, int percent, String key, int durationSeconds, Asgardus plugin) {
        int base = getStat(stat);
        int delta = (base * percent) / 100;
        addStat(stat, delta, plugin);
        PercentageBoost boost = new PercentageBoost(stat, percent, delta);
        percentageBoosts.put(key, boost);
        if (durationSeconds > 0) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                removePercentageBoost(key, plugin);
            }, durationSeconds * 20L);
        }
    }

    public void removePercentageBoost(String key, Asgardus plugin) {
        PercentageBoost boost = percentageBoosts.remove(key);
        if (boost != null) {
            addStat(boost.stat, -boost.delta, plugin);
        }
    }

    public void clearAllTempStatsAndBoosts(Asgardus plugin) {
        for (Runnable remover : tempBoostRemovers.values()) {
            remover.run();
        }
            tempBoostRemovers.clear();
        for (String key : percentageBoosts.keySet()) removePercentageBoost(key, plugin);
    }

    private static class PercentageBoost {
        final StatType stat;
        final int percent;
        final int delta;
        PercentageBoost(StatType stat, int percent, int delta) {
            this.stat = stat;
            this.percent = percent;
            this.delta = delta;
        }
    }
}