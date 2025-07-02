package ahjd.asgardus.statserivce.commands;

import ahjd.asgardus.statserivce.utils.StatType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument: action
            completions.addAll(Arrays.asList("set", "add", "reset"));
        } else if (args.length == 2) {
            // Second argument: stat type (only for set/add)
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")) {
                for (StatType stat : StatType.values()) {
                    completions.add(stat.name().toLowerCase());
                }
            }
        } else if (args.length == 3) {
            // Third argument: amount (for set/add)
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")) {
                completions.addAll(Arrays.asList("1", "10", "50", "100"));
            }
        } else if (args.length == 4 || (args.length == 2 && args[0].equalsIgnoreCase("reset"))) {
            // Fourth argument or second for reset: player names
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        // Filter completions based on what the user has typed
        String currentArg = args[args.length - 1].toLowerCase();
        completions.removeIf(completion -> !completion.toLowerCase().startsWith(currentArg));

        return completions;
    }
}