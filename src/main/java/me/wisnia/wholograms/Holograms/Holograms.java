package me.wisnia.wholograms.Holograms;

import me.wisnia.wholograms.Config.Config;
import me.wisnia.wholograms.Config.Saves;
import me.wisnia.wholograms.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class Holograms {

    public static Map<String, ArmorStand> hologramBase;

    public static Map<ArmorStand, List<ArmorStand>> hologramLines;

    public static Map<String, Integer> hologramTimeLine;

    public static void initialize() {
        hologramBase = new HashMap<>();
        hologramLines = new HashMap<>();
        hologramTimeLine = new HashMap<>();
    }

    public static void register() {
        if (Saves.saves.get("holograms") == null) return;
        for (String hologram : Objects.requireNonNull(Saves.saves.getConfigurationSection("holograms")).getKeys(false)) {
            World world = Bukkit.getWorld(Objects.requireNonNull(Saves.saves.getString("holograms." + hologram + ".world")));
            String[] locationTmp = Objects.requireNonNull(Saves.saves.getString("holograms." + hologram + ".location")).split(", ");
            Location location = new Location(world, Double.parseDouble(locationTmp[0]), Double.parseDouble(locationTmp[1]), Double.parseDouble(locationTmp[2]));
            assert world != null;
            Chunk chunk = world.getChunkAt(location);


            createHologram(hologram, location);
            for (String lines : Saves.saves.getStringList("holograms." + hologram + ".lines")) {
                addLine(hologram, getHologramBase(hologram), lines);
            }
        }
    }

    public static void clear() {
        for (World worlds : Bukkit.getServer().getWorlds()) {
            for (Entity entity : worlds.getEntities()) {
                for (String tags : entity.getScoreboardTags()) {
                    if (tags.equals("wHologram")) {
                        entity.remove();
                    }
                }
            }
        }
    }

    public static void createHologram(String key, Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.addScoreboardTag("wHologram");

        hologramBase.put(key, armorStand);
        hologramLines.put(armorStand, new ArrayList<>());
    }

    public static void addLine(String key, ArmorStand hologram, String text) {
        double height;
        if (getHologramLines(key) == 0) {
            height = hologram.getLocation().getY();
        } else {
            height = hologramLines.get(hologram).get(hologramLines.get(hologram).size() - 1).getLocation().getY();
        }
        height -= Config.config.getDouble("hologram-height");
        Location location = hologram.getLocation();
        location.setY(height);

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);

        Component line = MiniMessage.miniMessage().deserialize(text);
        armorStand.customName(line);

        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.addScoreboardTag("wHologram");

        List<ArmorStand> lines = hologramLines.get(hologram);
        lines.add(armorStand);

        hologramLines.put(hologram, lines);
    }

    public static ArmorStand getHologramBase(String key) {
        return hologramBase.get(key);
    }

    public static int getHologramLines(String key) {
        return hologramLines.get(hologramBase.get(key)).size();
    }

    public static void updateTime(String key) {
        ArmorStand base = hologramBase.get(key);
        List<String> configLines = Config.config.getStringList("settings.holograms.lines");
        int x = 0;
        for (ArmorStand line : hologramLines.get(base)) {
            String stringLine = configLines.get(x);
            line.setCustomName(Main.hexTranslate(stringLine));
            x++;
        }
    }

    public static String getCreatedDateTime(String key) {
        Saves.reload();
        return Saves.saves.getString("holograms." + key + ".created");
    }

    public static int getHologramsAmount() {
        if (Saves.saves.get("holograms") == null) return 0;
        int x = 0;
        for (String id : Objects.requireNonNull(Saves.saves.getConfigurationSection("holograms")).getKeys(false)) {
            x++;
        }
        return x;
    }

    public static boolean hologramExists(String key) {
        if (Saves.saves.get("holograms") == null) return false;
        for (String id : Objects.requireNonNull(Saves.saves.getConfigurationSection("holograms")).getKeys(false)) {
            if (id.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    public static void update() {
        clear();
        register();
    }

}
