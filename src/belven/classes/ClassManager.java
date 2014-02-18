package belven.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import belven.events.ClassChangeEvent;
import belven.listeners.BlockListener;
import belven.listeners.PlayerListener;
import belven.timedevents.ArenaTimer;

public class ClassManager extends JavaPlugin
{
    private final PlayerListener newplayerListener = new PlayerListener(this);
    private final BlockListener blockListener = new BlockListener(this);
    public HashMap<Player, Class> CurrentPlayerClasses = new HashMap<Player, Class>();

    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);

        Player[] currentPlayers = this.getServer().getOnlinePlayers();

        if (currentPlayers != null)
        {
            for (int i = 0; i < currentPlayers.length; i++)
            {
                Player currentPlayer = currentPlayers[i];

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

                    CurrentPlayerClasses.put(playerToAdd,
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
                for (int i = 0; i < currentPlayers.length; i++)
                {
                    Player currentPlayer = currentPlayers[i];

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
        else if (commandSent.equalsIgnoreCase("bcarena"))
        {
            new ArenaTimer(player.getLocation().getBlock(),
                    Integer.valueOf(args[0]), Material.getMaterial(args[1]))
                    .runTaskTimer(this, SecondsToTicks(1),
                            SecondsToTicks(Integer.valueOf(args[2])));
            return true;
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
        CurrentPlayerClasses.put(playerToChange,
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

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

}
