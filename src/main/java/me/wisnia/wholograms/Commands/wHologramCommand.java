package me.wisnia.wholograms.Commands;

import me.wisnia.wholograms.Config.Config;
import me.wisnia.wholograms.Config.Saves;
import me.wisnia.wholograms.Holograms.Holograms;
import me.wisnia.wholograms.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class wHologramCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.hexTranslate("&8★ &7Pomoc dotycząca hologramów:"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram list &8- &7lista stworzonych hologramów"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram create <id> &8- &7tworzy nowy hologram"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram lines <id> &8- &7wyświetla linijki tekstu hologramu"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram add <id> <tekst> &8- &7dodaje nową linijkę do hologramu"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram remove <id> &8- &7usuwa hologram"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram edit <id> <linijka> <tekst> &8- &7edytuje treść podanej linijki"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram tp <id> &8- &7teleportuje gracza do hologramu"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram movehere <id> &8- &7teleportuje hologram do gracza"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram removeline <id> <linijka> &8- &7usuwa linijkę tekstu hologramu"));
            sender.sendMessage(Main.hexTranslate("&8★ #037bfc/whologram reload &8- &7przeładowuje hologramy i plik konfiguracyjny"));
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("wholograms.list")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (Holograms.getHologramsAmount() == 0) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie ma żadnych stworzonych hologramów!"));
                return true;
            }
            sender.sendMessage(Main.hexTranslate("&8★ &7Lista hologramów:"));
            for (String id : Saves.saves.getConfigurationSection("holograms").getKeys(false)) {
                sender.sendMessage(Main.hexTranslate("&8★ &f" + id + " &8(&7" + Holograms.getCreatedDateTime(id) + "&8)"));
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission("wholograms.create")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie możesz użyć tej komendy!"));
                return false;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki juz istnieje!
            if (Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram istnieje!"));
                return false;
            }
            Saves.addHologram(name, player.getLocation());

            //odśwież
            Holograms.update();

            player.sendMessage(Main.hexTranslate("&8★ &7Pomyślnie stworzyłeś hologram o nazwie &a" + name + "&7!"));

            return true;
        } else if (args[0].equalsIgnoreCase("lines")) {
            if (!sender.hasPermission("wholograms.lines")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            sender.sendMessage(Main.hexTranslate("&8★ &7Tekst hologramu &a" + name + "&8:"));
            int lineNumber = 0;
            for (String lines : Saves.getHologramLines(name)) {
                Component line = MiniMessage.miniMessage().deserialize("<dark_gray>★</dark_gray> <gray>[</gray><white>" + lineNumber + "</white><gray>]</gray> <dark_gray>-</dark_gray> " + lines);
                sender.sendMessage(line);
                lineNumber++;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!sender.hasPermission("wholograms.add")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            if (args.length < 3) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj treść linijki!"));
                return false;
            }
            //stworz caly line
            String text = "";
            for (int x = 2; x <= args.length - 1; x++) {
                if (x != args.length - 1) {
                    text = text + args[x] + " ";
                } else {
                    text = text + args[x];
                }
            }
            //dodaj
            Saves.addLine(name, text.toString());
            //update
            Holograms.update();

            sender.sendMessage(Main.hexTranslate("&8★ &7Dodano nową linijkę do hologramu &a" + name + "&7!"));
            return true;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("wholograms.remove")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            Saves.removeHologram(name);
            sender.sendMessage(Main.hexTranslate("&8★ &7Usunięto hologram o nazwie &a" + name + "&7!"));
            Holograms.update();
            return true;
        } else if (args[0].equalsIgnoreCase("edit")) {
            if (!sender.hasPermission("wholograms.edit")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            if (args.length < 3) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj numer linijki!"));
                return false;
            }
            int line = 0;
            try {
                line = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodałeś błędny numer linijki!"));
                return false;
            }
            if (args.length < 4) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj treść linijki!"));
                return false;
            }
            String text = "";
            for (int x = 3; x <= args.length - 1; x++) {
                if (x != args.length - 1) {
                    text = text + args[x] + " ";
                } else {
                    text = text + args[x];
                }
            }

            //edit line
            List<String> lines = Saves.getHologramLines(name);
            lines.set(line - 1, text.toString());

            //update lines
            Saves.updateHologramLines(name, lines);

            //refresh
            Holograms.update();
            //notify
            sender.sendMessage(Main.hexTranslate("&8★ &7Edytowano linijkę &a" + line + " &7dla hologramu &a" + name + "&7!"));
            return true;
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (!sender.hasPermission("wholograms.tp")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie możesz tego zrobić!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            Player player = (Player) sender;
            player.teleport(Saves.getHologramLocation(name));
            player.sendMessage(Main.hexTranslate("&8★ &7Zostałeś przteleportowany do &a" + name + "&7!"));
            return true;
        } else if (args[0].equalsIgnoreCase("movehere")) {
            if (!sender.hasPermission("wholograms.movehere")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie możesz tego zrobić!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            Player player = (Player) sender;

            Saves.updateHologramLocation(name, player.getLocation());
            Holograms.update();

            player.sendMessage(Main.hexTranslate("&8★ &7Przeteleportowałeś hologram &a" + name + " &7do siebie!"));
            return true;
        } else if (args[0].equalsIgnoreCase("removeline")) {
            if (!sender.hasPermission("wholograms.removeline")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj nazwę hologramu!"));
                return false;
            }
            String name = args[1];
            //sprawdz czy hologram taki już istnieje!
            if (!Holograms.hologramExists(name)) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodany hologram nie istnieje!"));
                return false;
            }
            if (args.length < 3) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodaj linijkę hologramu!"));
                return false;
            }
            int line = 0;
            try {
                line = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodałeś błędny numer linijki!"));
                return false;
            }

            List<String> lines = Saves.getHologramLines(name);

            if (lines.size() < line) {
                sender.sendMessage(Main.hexTranslate("&8★ &cPodałeś błędny numer linijki!"));
                return false;
            }

            lines.remove(line - 1);
            Saves.updateHologramLines(name, lines);

            Holograms.update();

            sender.sendMessage(Main.hexTranslate("&8★ &7Usunąłeś linijkę &a" + line + " &7z hologramu &a" + name + "&7!"));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("wholograms.reload")) {
                sender.sendMessage(Main.hexTranslate("&8★ &cNie masz dostępu do tej komendy!"));
                return false;
            }
            Config.reload();
            Saves.reload();
            Holograms.clear();
            Holograms.register();
            sender.sendMessage(Main.hexTranslate("&8★ &6Przeładowałeś hologramy i plik konfiguracyjny!"));
            return true;
        }
        return true;
    }
}
