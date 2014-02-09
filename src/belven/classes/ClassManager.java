package belven.classes;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import belven.listeners.BlockListener;
import belven.listeners.PlayerListener;

public class ClassManager extends JavaPlugin
{
    private final PlayerListener newplayerListener = new PlayerListener(this);
    private final BlockListener blockListener = new BlockListener(this);
    public HashMap<Player, String> CurrentClasses = new HashMap<Player, String>();
    public HashMap<Player, Class> CurrentPlayerClasses = new HashMap<Player, Class>();

    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);
    }

    public void AddClassToPlayer(Player playerToAdd)
    {
        String PlayerName = playerToAdd.getPlayerListName().toLowerCase();
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
                CurrentClasses.put(playerToAdd, classString);
                CurrentPlayerClasses.put(playerToAdd,
                        StringToClass(classString, playerToAdd));

                this.getServer().broadcastMessage(
                        PlayerName + " was given class " + classString);
            }
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
                        String currentClass = CurrentClasses.get(currentPlayer);

                        if (currentClass != null)
                        {
                            this.getServer().broadcastMessage(
                                    currentPlayer.getName() + " is a "
                                            + currentClass);
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
                String currentClassName = CurrentClasses.get(player);

                if (currentClassName != null)
                {
                    Class currentClass = StringToClass(currentClassName, player);
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
        default:
            return new DEFAULT();
        }
    }

    public void SetClass(Player playerToChange, String classString)
    {
        String PlayerName = playerToChange.getPlayerListName().toLowerCase();
        this.reloadConfig();

        getConfig().set(PlayerName + ".Class", classString);
        getConfig().set(PlayerName + ".Level", playerToChange.getLevel());

        this.getServer().broadcastMessage(
                PlayerName + " is class " + classString);

        this.saveConfig();
        CurrentClasses.put(playerToChange, classString);
        CurrentPlayerClasses.put(playerToChange,
                StringToClass(classString, playerToChange));
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Goodbye world!");
    }

}
