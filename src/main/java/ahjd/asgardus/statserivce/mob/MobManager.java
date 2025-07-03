package ahjd.asgardus.statserivce.mob;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.utils.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MobManager {
    private final Map<UUID, MobData> mobDataMap = new ConcurrentHashMap<>();
    private final Asgardus plugin;

    public MobManager(Asgardus plugin) {
        this.plugin = plugin;
    }

    public UUID createMob(Map<StatType, Integer> stats, Tier tier, MobType mobType) {
        UUID uuid = UUID.randomUUID();
        MobData mobData = new MobData(uuid, stats, tier, mobType);
        mobDataMap.put(uuid, mobData);
        return uuid;
    }

    public void createMob(UUID uuid, Map<StatType, Integer> stats, Tier tier, MobType mobType) {
        MobData mobData = new MobData(uuid, stats, tier, mobType);
        mobDataMap.put(uuid, mobData);
    }

    public boolean editMob(UUID uuid, Map<StatType, Integer> newStats, Tier tier, MobType mobType) {
        MobData mob = mobDataMap.get(uuid);
        if (mob == null) return false;

        mob.setStats(newStats);
        mob.setTier(tier);
        mob.setMobType(mobType);
        return true;
    }

    public MobData getMob(UUID uuid) {
        return mobDataMap.get(uuid);
    }

    public boolean hasMob(UUID uuid) {
        return mobDataMap.containsKey(uuid);
    }

    public boolean removeMob(UUID uuid) {
        return mobDataMap.remove(uuid) != null;
    }

    public List<UUID> getAllMobsOfType(MobType mobType) {
        return mobDataMap.values().stream()
                .filter(mob -> mob.getMobType() == mobType)
                .map(MobData::getUuid)
                .collect(Collectors.toList());
    }

    public List<UUID> getAllMobsOfTier(Tier tier) {
        return mobDataMap.values().stream()
                .filter(mob -> mob.getTier() == tier)
                .map(MobData::getUuid)
                .collect(Collectors.toList());
    }

    public List<UUID> getAllMobs() {
        return new ArrayList<>(mobDataMap.keySet());
    }
}