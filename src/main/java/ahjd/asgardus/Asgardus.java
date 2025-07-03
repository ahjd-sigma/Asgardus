package ahjd.asgardus;

import ahjd.asgardus.statserivce.PlayerManager;
import ahjd.asgardus.statserivce.api.AsgStatsAPI;
import ahjd.asgardus.statserivce.commands.StatsCMD;
import ahjd.asgardus.statserivce.commands.StatsTabCompleter;
import ahjd.asgardus.statserivce.listeners.onJoinListener;
import ahjd.asgardus.statserivce.utils.StatSettings;
import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;

public final class Asgardus extends JavaPlugin {

    private final Map<StatType, StatSettings> statSettingsMap = new EnumMap<>(StatType.class);
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadDefaultStats();
        playerManager = new PlayerManager(this);
        AsgStatsAPI.init(this);

        if (getCommand("stats") != null) {
            getCommand("stats").setExecutor(new StatsCMD(this));
            getCommand("stats").setTabCompleter(new StatsTabCompleter());
        } else {
            getLogger().severe("Failed to register commands");
        }

        getServer().getPluginManager().registerEvents(new onJoinListener(this), this);
        getLogger().info("=== Asgardus Enabled ===");
    }

    public void loadDefaultStats() {
        ConfigurationSection section = getConfig().getConfigurationSection("default-stats");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                StatType stat = StatType.valueOf(key.toUpperCase());
                ConfigurationSection statSection = section.getConfigurationSection(key);

                int def = statSection.getInt("default", 0);
                int min = statSection.getInt("min", Integer.MIN_VALUE);
                int max = statSection.getInt("max", Integer.MAX_VALUE);

                statSettingsMap.put(stat, new StatSettings(def, min, max));

                // If HEALTH is defined, CURRENT_HEALTH mirrors it
                if (stat == StatType.HEALTH) {
                    statSettingsMap.put(StatType.CURRENT_HEALTH, new StatSettings(def, min, max));
                }

            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid stat in config: " + key);
            }
        }
    }

    public StatSettings getStatSettings(StatType stat) {
        // Mirror HEALTH config for CURRENT_HEALTH dynamically
        if (stat == StatType.CURRENT_HEALTH) {
            StatSettings health = statSettingsMap.get(StatType.HEALTH);
            if (health != null) return new StatSettings(health.getDefaultValue(), health.getMinValue(), health.getMaxValue());
        }
        return statSettingsMap.getOrDefault(stat, new StatSettings(0, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }


    public PlayerManager getPlayerManager(){
        return this.playerManager;
    }
}
