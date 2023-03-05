package me.wisnia.wholograms.Config;

import me.wisnia.wholograms.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    public static YamlConfiguration config;

    public static boolean exists() {
        File file = new File(Main.getPlugin().getDataFolder() + "/messages.yml");
        return file.exists();
    }

    public static void create() {
        Main.getPlugin().saveResource("messages.yml", true);
        reload();
    }

    public static void reload() {
        if (!exists()) create();
        File file = new File(Main.getPlugin().getDataFolder() + "/messages.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

}
