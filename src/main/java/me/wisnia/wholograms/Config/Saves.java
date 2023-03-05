package me.wisnia.wholograms.Config;

import me.wisnia.wholograms.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Saves {

    public static YamlConfiguration saves;

    public static boolean exists() {
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        return file.exists();
    }

    public static void create() {
        if (exists()) return;
        Main.getPlugin().saveResource("saves.yml", true);
        reload();
    }

    public static void reload() {
        if (!exists()) create();
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        saves = YamlConfiguration.loadConfiguration(file);
    }

    public static void addHologram(String key, Location location) {
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        saves.set("holograms." + key + ".created", dateTimeFormatter.format(now));
        saves.set("holograms." + key + ".world", location.getWorld().getName());
        saves.set("holograms." + key + ".location", location.getX() + ", " + location.getY() + ", " + location.getZ());
        List<String> lines = new ArrayList<>();
        lines.add("<white>Podstawowy tekst</white>");
        saves.set("holograms." + key + ".lines", lines);
        try {
            saves.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getHologramLines(String key) {
        return saves.getStringList("holograms." + key + ".lines");
    }

    public static void addLine(String key, String text) {
        reload();
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        List<String> lines = saves.getStringList("holograms." + key + ".lines");
        lines.add(text);
        saves.set("holograms." + key + ".lines", lines);
        try {
            saves.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeHologram(String key) {
        reload();
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        saves.set("holograms." + key, null);
        try {
            saves.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateHologramLines(String key, List<String> lines) {
        reload();
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        saves.set("holograms." + key + ".lines", lines);
        try {
            saves.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Location getHologramLocation(String key) {
        reload();
        World world = Bukkit.getWorld(Saves.saves.getString("holograms." + key + ".world"));
        String[] tmp = Saves.saves.getString("holograms." + key + ".location").split(", ");
        return new Location(world, Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]), Double.parseDouble(tmp[2]));
    }

    public static void updateHologramLocation(String key, Location location) {
        reload();
        File file = new File(Main.getPlugin().getDataFolder() + "/saves.yml");
        saves.set("holograms." + key + ".world", location.getWorld().getName());
        String loc = location.getX() + ", " + location.getY() + ", " + location.getZ();
        saves.set("holograms." + key + ".location", loc);
        try {
            saves.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
