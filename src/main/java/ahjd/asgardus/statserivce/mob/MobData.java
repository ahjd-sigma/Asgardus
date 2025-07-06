package ahjd.asgardus.statserivce.mob;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

public class MobData {
    private final UUID uuid;
    private final Map<StatType, Integer> stats;
    private Tier tier;
    private MobType mobType;
    private Behaviour behaviour;
    private CombatType combatType;

    public MobData(UUID uuid, Map<StatType, Integer> stats, Tier tier, MobType mobType, Behaviour behaviour, CombatType combatType) {
        this.uuid = uuid;
        this.stats = new EnumMap<>(StatType.class);
        this.stats.putAll(stats);
        this.tier = tier;
        this.mobType = mobType;
        this.behaviour = behaviour;
        this.combatType = combatType;
    }

    public UUID getUuid() { return uuid; }
    public Map<StatType, Integer> getStats() { return new EnumMap<>(stats); }
    public Tier getTier() { return tier; }
    public MobType getMobType() { return mobType; }
    public Behaviour getBehaviour() { return behaviour; }
    public CombatType getCombatType() {return combatType; }

    public void setTier(Tier tier) { this.tier = tier; }
    public void setMobType(MobType mobType) { this.mobType = mobType; }
    public void setBehaviour(Behaviour behaviour) { this.behaviour = behaviour; }
    public void setCombatType(CombatType combatType){this.combatType = combatType;}

    public int getStat(StatType stat) { return stats.getOrDefault(stat, 0); }
    public void setStat(StatType stat, int value) { stats.put(stat, value); }

    public void setStats(Map<StatType, Integer> newStats) {
        stats.clear();
        stats.putAll(newStats);
    }

    private final Map<String, Runnable> tempBoostRemovers = new ConcurrentHashMap<>();
    private final Map<String, Long> boostExpiryTimes = new ConcurrentHashMap<>();
    private final Map<String, Integer> flatBoostValues = new ConcurrentHashMap<>();
    private final Map<String, PercentageBoost> percentageBoosts = new ConcurrentHashMap<>();
    private final Map<String, BoostRecord> activeBoosts = new ConcurrentHashMap<>();

    private static class BoostRecord {
        final StatType stat;
        final int value;
        final boolean isPercentage;
        final long expiryTime;

        BoostRecord(StatType stat, int value, boolean isPercentage, long expiryTime) {
            this.stat = stat;
            this.value = value;
            this.isPercentage = isPercentage;
            this.expiryTime = expiryTime;
        }
    }

    // Update addTempStat method
    public void addTempStat(StatType stat, int amount, int durationSeconds, String key, Asgardus plugin) {
        int currentValue = getStat(stat);
        int newValue = currentValue + amount;
        setStat(stat, newValue);

        long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        activeBoosts.put(key, new BoostRecord(stat, amount, false, expiryTime));

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            removeTempStat(key);
        }, durationSeconds * 20L);
    }

    // Update addPercentageBoost method
    public void addPercentageBoost(StatType stat, int percent, String key, int durationSeconds, Asgardus plugin) {
        int base = getStat(stat);
        int delta = (base * percent) / 100;
        setStat(stat, base + delta);

        long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        activeBoosts.put(key, new BoostRecord(stat, percent, true, expiryTime));

        if (durationSeconds > 0) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                removePercentageBoost(key);
            }, durationSeconds * 20L);
        }
    }

    // Update remove methods
    public void removeTempStat(String key) {
        BoostRecord record = activeBoosts.remove(key);
        if (record != null && !record.isPercentage) {
            setStat(record.stat, getStat(record.stat) - record.value);
        }
    }

    public void removePercentageBoost(String key) {
        BoostRecord record = activeBoosts.remove(key);
        if (record != null && record.isPercentage) {
            int current = getStat(record.stat);
            int base = (current * 100) / (100 + record.value);
            setStat(record.stat, base);
        }
    }

    // Update clear method
    public void clearAllTempStatsAndBoosts() {
        for (String key : new ArrayList<>(activeBoosts.keySet())) {
            BoostRecord record = activeBoosts.get(key);
            if (record.isPercentage) {
                removePercentageBoost(key);
            } else {
                removeTempStat(key);
            }
        }
    }

    // Add new methods
    public Map<StatType, Integer> getActiveFlatBoosts() {
        Map<StatType, Integer> boosts = new EnumMap<>(StatType.class);
        for (BoostRecord record : activeBoosts.values()) {
            if (!record.isPercentage) {
                boosts.merge(record.stat, record.value, Integer::sum);
            }
        }
        return boosts;
    }

    public Map<StatType, Integer> getActivePercentBoosts() {
        Map<StatType, Integer> boosts = new EnumMap<>(StatType.class);
        for (BoostRecord record : activeBoosts.values()) {
            if (record.isPercentage) {
                boosts.merge(record.stat, record.value, Integer::sum);
            }
        }
        return boosts;
    }

    public int getBoostRemainingTime(StatType stat) {
        long now = System.currentTimeMillis();
        return Math.toIntExact(activeBoosts.values().stream()
                .filter(record -> record.stat == stat)
                .mapToLong(record -> (record.expiryTime - now) / 1000)
                .filter(time -> time > 0)
                .min()
                .orElse(0));
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