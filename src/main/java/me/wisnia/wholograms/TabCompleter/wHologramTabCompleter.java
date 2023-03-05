package me.wisnia.wholograms.TabCompleter;

import me.wisnia.wholograms.Config.Saves;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class wHologramTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("wholograms.usage")) return null;
        if (args.length < 2) {
            List<String> completions = new ArrayList<>();
            completions.add("list");
            completions.add("create");
            completions.add("lines");
            completions.add("add");
            completions.add("remove");
            completions.add("edit");
            completions.add("tp");
            completions.add("movehere");
            completions.add("removeline");
            completions.add("reload");
            return completions;
        }
        if (args.length < 3) {
            if (args[0].equalsIgnoreCase("lines") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("movehere") || args[0].equalsIgnoreCase("removeline")) {
                List<String> completions = new ArrayList<>();
                for (String hologram : Saves.saves.getConfigurationSection("holograms").getKeys(false)) {
                    completions.add(hologram);
                }
                return completions;
            }
        }
        if (args.length < 4) {
            if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("removeline")) {
                List<String> completions = new ArrayList<>();
                List<String> lines = Saves.getHologramLines(args[1]);
                for (int x = 1; x <= lines.size(); x++) {
                    completions.add(String.valueOf(x));
                }
                return completions;
            }
        }
        return null;
    }
}
