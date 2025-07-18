package ahjd.asgardus.statserivce.commands;

import ahjd.asgardus.Asgardus;
import ahjd.asgardus.statserivce.PlayerData;
import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCMD implements CommandExecutor {

    private final Asgardus plugin;

    public StatsCMD(Asgardus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /stats - view own stats
        if (args.length == 0) {
            if (sender instanceof Player player) {
                PlayerData data = plugin.getPlayerManager().getOrCreate(player.getUniqueId());
                sender.sendMessage("§a[Stats]");
                for (StatType stat : StatType.values()) {
                    sender.sendMessage("§7" + stat.name() + ": §b" + data.getStat(stat));
                }
            } else {
                sender.sendMessage("§cOnly players can use this without arguments.");
            }
            return true;
        }

        // /stats reset [player]
        if (args[0].equalsIgnoreCase("reset")) {
            UUID targetUUID;
            String targetName;

            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cCould not find player: " + args[1]);
                    return true;
                }
                targetUUID = target.getUniqueId();
                targetName = target.getName();
            } else if (sender instanceof Player player) {
                targetUUID = player.getUniqueId();
                targetName = player.getName();
            } else {
                sender.sendMessage("§cConsole must specify a player.");
                return true;
            }

            PlayerData data = plugin.getPlayerManager().getOrCreate(targetUUID);
            for (StatType stat : StatType.values()) {
                int def = plugin.getStatSettings(stat).getDefaultValue();
                data.setStat(stat, def, plugin);
            }

            sender.sendMessage("§aReset stats to defaults for §b" + targetName);
            return true;
        }

        // /stats set|add <stat> <amount> [player]
        if (args.length < 3 || args.length > 4) {
            sender.sendMessage("§cUsage: /stats [set|add|reset] <stat> <value> [player]");
            return true;
        }

        String action = args[0].toLowerCase();
        String statName = args[1].toUpperCase();
        String amountStr = args[2];

        StatType stat;
        try {
            stat = StatType.valueOf(statName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid stat: " + statName);
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number: " + amountStr);
            return true;
        }

        UUID targetUUID;
        String targetName;

        if (args.length == 4) {
            Player target = Bukkit.getPlayer(args[3]);
            if (target == null) {
                sender.sendMessage("§cCould not find player: " + args[3]);
                return true;
            }
            targetUUID = target.getUniqueId();
            targetName = target.getName();
        } else if (sender instanceof Player player) {
            targetUUID = player.getUniqueId();
            targetName = player.getName();
        } else {
            sender.sendMessage("§cConsole must specify a player.");
            return true;
        }

        PlayerData data = plugin.getPlayerManager().getOrCreate(targetUUID);
        int min = plugin.getStatSettings(stat).getMinValue();
        int max = plugin.getStatSettings(stat).getMaxValue();

        switch (action) {
            case "set" -> {
                data.setStat(stat, amount, plugin);
                int actual = data.getStat(stat);
                sender.sendMessage("§aSet §b" + stat.name() + " §ato §e" + actual + " §afor §b" + targetName);
                if (actual != amount) {
                    sender.sendMessage("§eNote: value was clamped between " + min + " and " + max);
                }
            }
            case "add" -> {
                int before = data.getStat(stat);
                data.addStat(stat, amount, plugin);
                int actual = data.getStat(stat);
                sender.sendMessage("§aAdded §e" + amount + " §ato §b" + stat.name() + " §afor §b" + targetName + ". Now: §b" + actual);
                if (actual != before + amount) {
                    sender.sendMessage("§eNote: value was clamped between " + min + " and " + max);
                }
            }
            default -> sender.sendMessage("§cUnknown action. Use: set, add, reset");
        }

        return true;
    }
}
