package ahjd.asgardus.statserivce;

import ahjd.asgardus.Asgardus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final Asgardus plugin;

    public PlayerManager(Asgardus plugin) {
        this.plugin = plugin;
    }

    public PlayerData getOrCreate(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, id -> new PlayerData(id, plugin));
    }

    public boolean has(UUID uuid) {
        return playerDataMap.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public Map<UUID, PlayerData> getAll() {
        return playerDataMap;
    }
}
