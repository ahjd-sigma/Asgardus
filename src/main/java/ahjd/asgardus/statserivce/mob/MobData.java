package ahjd.asgardus.statserivce.mob;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatType;
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

    public void removeTempStat(String key) {
        Runnable remover = tempBoostRemovers.remove(key);
        if (remover != null) remover.run();
        boostExpiryTimes.remove(key);
    }

    public void addTempStat(StatType stat, int amount, int durationSeconds, String key, Asgardus plugin) {
        int currentValue = getStat(stat);
        int newValue = currentValue + amount;
        setStat(stat, newValue);

        flatBoostValues.put(key, amount);
        long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        boostExpiryTimes.put(key, expiryTime);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            removeTempStat(key);
        }, durationSeconds * 20L);
    }

    public void addPercentageBoost(StatType stat, int percent, String key, int durationSeconds, Asgardus plugin) {
        int base = getStat(stat);
        int delta = (base * percent) / 100;
        setStat(stat, base + delta);

        PercentageBoost boost = new PercentageBoost(stat, percent, delta);
        percentageBoosts.put(key, boost);
        long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        boostExpiryTimes.put(key, expiryTime);

        if (durationSeconds > 0) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                removePercentageBoost(key);
            }, durationSeconds * 20L);
        }
    }


    public void removePercentageBoost(String key) {
        PercentageBoost boost = percentageBoosts.remove(key);
        if (boost != null) {
            setStat(boost.stat, getStat(boost.stat) - boost.delta);
        }
        boostExpiryTimes.remove(key);
    }

    public void clearAllTempStatsAndBoosts() {
        for (Runnable remover : tempBoostRemovers.values()) remover.run();
        tempBoostRemovers.clear();
        for (String key : percentageBoosts.keySet()) removePercentageBoost(key);
    }

    public Map<StatType, Integer> getActiveFlatBoosts() {
        Map<StatType, Integer> boosts = new EnumMap<>(StatType.class);
        for (Map.Entry<String, Integer> entry : flatBoostValues.entrySet()) {
            StatType stat = StatType.valueOf(entry.getKey().split("_")[2]); // Extract stat from key
            boosts.merge(stat, entry.getValue(), Integer::sum);
        }
        return boosts;
    }

    public Map<StatType, Integer> getActivePercentBoosts() {
        Map<StatType, Integer> boosts = new EnumMap<>(StatType.class);
        for (PercentageBoost boost : percentageBoosts.values()) {
            boosts.merge(boost.stat, boost.percent, Integer::sum);
        }
        return boosts;
    }

    public int getBoostRemainingTime(StatType stat) {
        long now = System.currentTimeMillis();
        return (int) boostExpiryTimes.entrySet().stream()
                .filter(entry -> {
                    String key = entry.getKey();
                    return (key.contains("flat") && key.contains(stat.name())) ||
                            (key.contains("percent") && percentageBoosts.containsKey(key) &&
                                    percentageBoosts.get(key).stat == stat);
                })
                .mapToLong(entry -> (entry.getValue() - now) / 1000)
                .min()
                .orElse(0);
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