package belven.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import resources.PlayerExtended;
import belven.arena.ArenaManager;
import belven.classes.events.ClassChangeEvent;
import belven.classes.listeners.BlockListener;
import belven.classes.listeners.MobListener;
import belven.classes.listeners.PlayerListener;
import belven.teams.TeamManager;

public class ClassManager extends JavaPlugin
{
    private final PlayerListener newplayerListener = new PlayerListener(this);
    private final BlockListener blockListener = new BlockListener(this);
    private final MobListener mobListener = new MobListener(this);

    public ArenaManager arenas = (ArenaManager) Bukkit.getServer()
            .getPluginManager().getPlugin("BelvensArenas");
    public TeamManager teams = (TeamManager) Bukkit.getServer()
            .getPluginManager().getPlugin("BelvensTeams");

    private HashMap<Player, Class> CurrentPlayerClasses = new HashMap<Player, Class>();
    public HashMap<Player, PlayerExtended> PlayersE = new HashMap<Player, PlayerExtended>();

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(mobListener, this);

        for (Player currentPlayer : getServer().getOnlinePlayers())
        {
            if (currentPlayer != null)
            {
                AddClassToPlayer(currentPlayer);
            }
        }
    }

    public PlayerExtended GetPlayerE(Player p)
    {
        return PlayersE.get(p);
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

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args)
    {
        Player player = (Player) sender;
        String commandSent = cmd.getName();

        if ((this.arenas != null) && (this.arenas.IsPlayerInArena(player)))
        {
            if (this.arenas.getArenaInIsPlayer(player).isActive)
            {
                return false;
            }
        }

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
        // else if (commandSent.equalsIgnoreCase("bcmage"))
        // {
        // this.SetClass(player, "Mage");
        //
        // return true;
        // }
        else if (commandSent.equalsIgnoreCase("bcwarrior"))
        {
            this.SetClass(player, "Warrior");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcmonk"))
        {
            this.SetClass(player, "Monk");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcpriest"))
        {
            this.SetClass(player, "Priest");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcdaemon"))
        {
            this.SetClass(player, "Daemon");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcberserker"))
        {
            this.SetClass(player, "Berserker");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcassassin"))
        {
            this.SetClass(player, "Assassin");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("setlevel"))
        {
            int level = Integer.valueOf(args[0]);
            player.setLevel(level);

            String temp1 = String.valueOf(Math.log(player.getLevel()));
            String temp2 = String.valueOf(Math.log10(player.getLevel()));
            String temp3 = String.valueOf(Math.log1p(player.getLevel()));

            player.sendMessage(temp1);
            player.sendMessage(temp2);
            player.sendMessage(temp3);
            return true;
        }
        else if (commandSent.equalsIgnoreCase("bcarcher"))
        {
            this.SetClass(player, "Archer");
            return true;
        }
        else if (commandSent.equalsIgnoreCase("listclasses"))
        {
            Player[] currentPlayers = this.getServer().getOnlinePlayers();
            for (Player currentPlayer : currentPlayers)
            {
                if (currentPlayer != null)
                {
                    Class currentClass = CurrentPlayerClasses
                            .get(currentPlayer);
                    player.sendMessage(currentPlayer.getName() + " is a "
                            + currentClass.getClassName());
                }
            }
            return true;
        }
        else if (commandSent.equalsIgnoreCase("listabilities"))
        {
            CurrentPlayerClasses.get(player).ListAbilities();
            return true;
        }
        else
            return false;
    }

    private Class StringToClass(String className, Player player)
    {
        switch (className.toLowerCase())
        {
        case "healer":
            return new Healer(player, this);
            // case "mage":
            // return new Mage(player, this);
        case "mage":
            return new DEFAULT(player, this);
        case "assassin":
            return new Assassin(player, this);
        case "archer":
            return new Archer(player, this);
        case "monk":
            return new Monk(player, this);
        case "daemon":
            return new Daemon(player, this);
        case "priest":
            return new Priest(player, this);
        case "warrior":
            return new Warrior(player, this);
        case "berserker":
            return new Berserker(player, this);
        default:
            return new DEFAULT(player, this);
        }
    }

    public Class GetClass(Player p)
    {
        return CurrentPlayerClasses.get(p);
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

}
