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
    private final Map<String, PercentageBoost> percentageBoosts = new ConcurrentHashMap<>();

    public void addTempStat(StatType stat, int amount, int durationSeconds, String key, Asgardus plugin) {
        setStat(stat, getStat(stat) + amount);
        Runnable remover = () -> setStat(stat, getStat(stat) - amount);
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

    public void addPercentageBoost(StatType stat, int percent, String key, int durationSeconds, Asgardus plugin) {
        int base = getStat(stat);
        int delta = (base * percent) / 100;
        setStat(stat, base + delta);
        PercentageBoost boost = new PercentageBoost(stat, percent, delta);
        percentageBoosts.put(key, boost);
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
    }

    public void clearAllTempStatsAndBoosts() {
        for (Runnable remover : tempBoostRemovers.values()) remover.run();
        tempBoostRemovers.clear();
        for (String key : percentageBoosts.keySet()) removePercentageBoost(key);
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