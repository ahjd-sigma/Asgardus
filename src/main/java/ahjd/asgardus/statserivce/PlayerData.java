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
    public void addTempStat(StatType stat, int amount, int durationSeconds, Asgardus plugin) {
        addStat(stat, amount, plugin);
        Runnable remover = () -> addStat(stat, -amount, plugin);
        tempBoostRemovers.add(remover);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            remover.run();
            tempBoostRemovers.remove(remover);
        }, durationSeconds * 20L);
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
        for (Runnable remover : tempBoostRemovers) {
            remover.run();
        }
        tempBoostRemovers.clear();
    }
    // Track active temp boosts for clearing
    private final Set<Runnable> tempBoostRemovers = ConcurrentHashMap.newKeySet();
}