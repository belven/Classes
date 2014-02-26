package belven.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import belven.classes.events.ClassChangeEvent;
import belven.classes.listeners.BlockListener;
import belven.classes.listeners.MobListener;
import belven.classes.listeners.PlayerListener;

public class ClassManager extends JavaPlugin
{
    private final PlayerListener newplayerListener = new PlayerListener(this);
    private final BlockListener blockListener = new BlockListener(this);
    private final MobListener mobListener = new MobListener(this);

    public HashMap<String, Class> CurrentPlayerClasses = new HashMap<String, Class>();

    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(mobListener, this);

        if (this.getServer().getOnlinePlayers().length > 0)
        {
            for (Player currentPlayer : this.getServer().getOnlinePlayers())
            {
                if (currentPlayer != null)
                {
                    AddClassToPlayer(currentPlayer);
                }
            }
        }
    }

    public void AddClassToPlayer(Player playerToAdd)
    {
        String PlayerName = playerToAdd.getName();
        this.reloadConfig();

        if (!this.getConfig().contains(PlayerName))
        {
            SetClass(playerToAdd, "DEFAULT");
        }
        else
        {
            String classString = this.getConfig().getString(
                    PlayerName + ".Class");

            if (classString != null)
            {
                if (CurrentPlayerClasses.get(playerToAdd) == null)

                    CurrentPlayerClasses.put(playerToAdd.getName(),
                            StringToClass(classString, playerToAdd));
            }

            this.getServer().broadcastMessage(
                    PlayerName + " was given class " + classString);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args)
    {
        Player[] currentPlayers = this.getServer().getOnlinePlayers();
        Player player = (Player) sender;
        String commandSent = cmd.getName();

        if (commandSent.equalsIgnoreCase("bchealer"))
        {
            this.SetClass(player, "Healer");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcdummy"))
        {
            Location playerLocation = player.getLocation();
            playerLocation.setX(playerLocation.getX() + 3);
            player.getWorld().spawnEntity(playerLocation, EntityType.ZOMBIE);

            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcmage"))
        {
            this.SetClass(player, "Mage");

            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcwarrior"))
        {
            this.SetClass(player, "Warrior");

            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcassassin"))
        {
            this.SetClass(player, "Assassin");

            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcarcher"))
        {
            this.SetClass(player, "Archer");

            return true;
        }
        else if (commandSent.equalsIgnoreCase("listclasses"))
        {
            if (currentPlayers != null)
            {
                for (Player currentPlayer : currentPlayers)
                {
                    if (currentPlayer != null)
                    {
                        Class currentClass = CurrentPlayerClasses
                                .get(currentPlayer);

                        if (currentClass != null)
                        {
                            this.getServer().broadcastMessage(
                                    currentPlayer.getName() + " is a "
                                            + currentClass.getClassName());
                        }
                    }
                }

                return true;
            }
            else
                return false;
        }
        else if (commandSent.equalsIgnoreCase("listabilities"))
        {
            if (player != null)
            {
                Class currentClass = CurrentPlayerClasses.get(player);
                if (currentClass != null)
                    this.getServer().broadcastMessage(
                            currentClass.ListAbilities());
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    public Class StringToClass(String className, Player player)
    {
        switch (className.toLowerCase())
        {
        case "healer":
            return new Healer(player, this);
        case "mage":
            return new Mage(player, this);
        case "assassin":
            return new Assassin(player, this);
        case "archer":
            return new Archer(player, this);
        case "warrior":
            return new Warrior(player, this);
        default:
            return new DEFAULT();
        }
    }

    public void SetClass(Player playerToChange, String classString)
    {
        String PlayerName = playerToChange.getName();

        this.reloadConfig();
        getConfig().set(PlayerName + ".Class", classString);

        playerToChange.sendMessage(PlayerName + " is class " + classString);

        this.saveConfig();
        CurrentPlayerClasses.put(playerToChange.getName(),
                StringToClass(classString, playerToChange));

        Bukkit.getPluginManager().callEvent(
                new ClassChangeEvent(CurrentPlayerClasses.get(playerToChange),
                        playerToChange));
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Goodbye world!");
        this.saveConfig();
    }

    public Location StringToLocation(String s, World world)
    {
        String[] strings = s.split(",");
        int x = Integer.valueOf(strings[0].trim());
        int y = Integer.valueOf(strings[1].trim());
        int z = Integer.valueOf(strings[2].trim());

        return new Location(world, x, y, z);
    }

    public String LocationToString(Block block)
    {
        String locationString = "";
        Location l = block.getLocation();

        locationString = String.valueOf(l.getBlockX()) + ","
                + String.valueOf(l.getBlockY()) + ","
                + String.valueOf(l.getBlockZ());
        return locationString;
    }

    public String LocationToString(Location l)
    {
        String locationString = "";

        locationString = String.valueOf(l.getBlockX()) + ","
                + String.valueOf(l.getBlockY()) + ","
                + String.valueOf(l.getBlockZ());
        return locationString;
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

}
