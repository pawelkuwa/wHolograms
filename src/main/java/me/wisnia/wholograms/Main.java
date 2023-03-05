package me.wisnia.wholograms;

import me.wisnia.wholograms.Commands.wHologramCommand;
import me.wisnia.wholograms.Config.Config;
import me.wisnia.wholograms.Config.Messages;
import me.wisnia.wholograms.Config.Saves;
import me.wisnia.wholograms.Holograms.Holograms;
import me.wisnia.wholograms.TabCompleter.wHologramTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public static Pattern pattern;

    @Override
    public void onEnable() {
        plugin = this;
        pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        // Plik konfiguracyjny (config.yml)
        if (!Config.exists()) {
            log("&cNie znalazłem pliku config.yml, tworzę go");
            Config.create();
        }
        log("&aPlik config.yml został przeładowany!");
        Config.reload();

        // Plik konfiguracyjny (messages.yml)
        if (!Messages.exists()) {
            log("&cNie znalazłem pliku config.yml, tworzę go");
            Messages.create();
        }
        log("&aPlik config.yml został przeładowany!");
        Messages.reload();

        // Plik z hologramami
        if (!Saves.exists()) Saves.create();
        Saves.reload();

        Holograms.initialize();
        Holograms.clear();
        Holograms.register();

        // Komendy
        Objects.requireNonNull(getCommand("whologram")).setExecutor(new wHologramCommand());
        Objects.requireNonNull(getCommand("whologram")).setTabCompleter(new wHologramTabCompleter());

    }

    public static String hexTranslate(String m) {
        Matcher matcher = pattern.matcher(m);
        while (matcher.find()) {
            String color = m.substring(matcher.start(), matcher.end());
            m = m.replace(color, ChatColor.valueOf(color) + "");
            matcher = pattern.matcher(m);
        }
        return ChatColor.translateAlternateColorCodes('&', m);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(Main.hexTranslate("&8[&2wHolograms&8] &r" + msg));
    }

}
