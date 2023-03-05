package me.wisnia.wholograms.Config;

import me.wisnia.wholograms.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    public static YamlConfiguration config;

    public static boolean exists() {
        File file = new File(Main.getPlugin().getDataFolder() + "/config.yml");
        return file.exists();
    }

    public static void create() {
        if (exists()) return;
        Main.getPlugin().saveResource("config.yml", true);
    }

    public static void reload() {
        if (!exists()) create();
        File file = new File(Main.getPlugin().getDataFolder() + "/config.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

}
