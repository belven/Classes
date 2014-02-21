package belven.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import belven.classes.blocks.ArenaBlock;
import belven.events.ClassChangeEvent;
import belven.listeners.ArenaListener;
import belven.listeners.BlockListener;
import belven.listeners.MobListener;
import belven.listeners.PlayerListener;

public class ClassManager extends JavaPlugin
{
    private final PlayerListener newplayerListener = new PlayerListener(this);
    private final BlockListener blockListener = new BlockListener(this);
    private final ArenaListener arenaListener = new ArenaListener(this);
    private final MobListener mobListener = new MobListener(this);

    public HashMap<Player, Class> CurrentPlayerClasses = new HashMap<Player, Class>();
    public List<ArenaBlock> currentArenaBlocks = new ArrayList<ArenaBlock>();
    public ArenaBlock currentArenaBlock;

    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(arenaListener, this);
        pm.registerEvents(mobListener, this);

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
            if (args[0].contains("select"))
            {
                SelectArena(player, args[1]);
                return true;
            }
            else if (args[0].contains("setactivateblock"))
            {
                MoveArenaBlock(player);
                return true;
            }
            else if (args[0].contains("setplayerblock"))
            {
                SetPlayerBlock(player);
                return true;
            }
            else if (args[0].contains("setwavetimer"))
            {
                SetWaveTimer(player, args[1]);
                return true;
            }
            else
            {
                ArenaBlockCreated(player, player.getLocation().getBlock(), args);
                return true;
            }
        }
        else
            return false;
    }

    private void SetWaveTimer(Player currentPlayer, String newPeriod)
    {
        int period = SecondsToTicks(Integer.valueOf(newPeriod));

        if (currentArenaBlock == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            currentArenaBlock.timerPeriod = period;
            currentPlayer.sendMessage(currentArenaBlock.arenaName
                    + " mobs now spawn every " + newPeriod);
        }
    }

    private void SetPlayerBlock(Player currentPlayer)
    {
        if (currentArenaBlock == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            currentArenaBlock.LocationToCheckForPlayers = currentPlayer
                    .getLocation();
            currentPlayer.sendMessage(currentArenaBlock.arenaName
                    + " player block has moved!");
        }

    }

    private void MoveArenaBlock(Player currentPlayer)
    {
        if (currentArenaBlock == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            Block tempBlock = currentArenaBlock.blockToActivate;
            tempBlock.removeMetadata("ArenaBlock", this);

            tempBlock = currentPlayer.getLocation().getBlock();
            tempBlock.setType(Material.STONE);
            tempBlock.setMetadata("ArenaBlock", new FixedMetadataValue(this,
                    "Something"));
            currentArenaBlock.blockToActivate = tempBlock;
            currentPlayer.sendMessage(currentArenaBlock.arenaName
                    + " active block has moved!");
        }

    }

    private void SelectArena(Player currentPlayer, String arenaToSelect)
    {
        for (int i = 0; i < currentArenaBlocks.size(); i++)
        {
            ArenaBlock tempArenaBlock = currentArenaBlocks.get(i);
            if (tempArenaBlock != null)
            {
                if (tempArenaBlock.arenaName.contains(arenaToSelect))
                {
                    currentArenaBlock = tempArenaBlock;
                    currentPlayer.sendMessage(arenaToSelect
                            + " is now selected");
                    break;
                }
            }
        }
    }

    public void ArenaBlockCreated(Player currentPlayer, Block block,
            String[] args)
    {
        try
        {
            block.setType(Material.STONE);
            block.setMetadata("ArenaBlock", new FixedMetadataValue(this,
                    "Something"));

            ArenaBlock newArenaBlock = new ArenaBlock(block, args[0],
                    Integer.valueOf(args[1]), Material.getMaterial(args[2]),
                    this, SecondsToTicks(1),
                    SecondsToTicks(Integer.valueOf(args[3])));

            currentArenaBlock = newArenaBlock;
            currentArenaBlocks.add(newArenaBlock);
        }
        catch (NumberFormatException err)
        {
            // currentPlayer.sendMessage("")
        }
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
