package belven.classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.MicrosoftSQL;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
    public HashMap<String, ArenaBlock> SelectedArenaBlocks = new HashMap<String, ArenaBlock>();
    private Database sql;

    private static String queryStringSep = "', '";
    private static String queryNumberSep = ", ";
    private static String queryStringToNumberSep = "', ";
    private static String queryNumberToStringSep = ", '";

    @Override
    public void onEnable()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(newplayerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(arenaListener, this);
        pm.registerEvents(mobListener, this);

        try
        {
            sql = new MicrosoftSQL(Logger.getLogger("Minecraft"), "Something",
                    "f0bh84aran.database.windows.net", 3306, "arenas", "USER",
                    "PASS");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        sql.open();

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

        RecreateArenas();
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
            else if (args[0].contains("remove"))
            {
                RemoveArenaBlock(player);
                return true;
            }
            else if (args[0].contains("setplayerblock"))
            {
                SetPlayerBlock(player);
                return true;
            }
            else if (args[0].contains("setwarpblock"))
            {
                SetWarpBlock(player);
                return true;
            }
            else if (args[0].contains("warp"))
            {
                WarpToArena(player, args[1]);
                return true;
            }
            else if (args[0].contains("setwavetimer"))
            {
                SetWaveTimer(player, args[1]);
                return true;
            }
            else if (args[0].contains("setwaves"))
            {
                SetWaves(player, args[1]);
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

    private void SetWaves(Player currentPlayer, String runtimes)
    {
        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            SelectedArenaBlocks.get(currentPlayer.getName()).maxRunTimes = Integer
                    .valueOf(runtimes);
            currentPlayer.sendMessage(SelectedArenaBlocks.get(currentPlayer
                    .getName()).arenaName + " waves set to " + runtimes);
        }
    }

    private void WarpToArena(Player player, String arenaToWarp)
    {
        ArenaBlock tempArenaBlock = getArenaBlock(arenaToWarp);
        if (tempArenaBlock != null)
        {
            player.teleport(tempArenaBlock.arenaWarp.getLocation());
            player.sendMessage("Teleport to " + arenaToWarp);
        }
        else
        {
            player.sendMessage("Can't find arena " + arenaToWarp);
        }
    }

    private void SetWarpBlock(Player currentPlayer)
    {
        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            SelectedArenaBlocks.get(currentPlayer.getName()).arenaWarp = currentPlayer
                    .getLocation().getBlock();
            currentPlayer.sendMessage(SelectedArenaBlocks.get(currentPlayer
                    .getName()).arenaName + " waarp block set!!");
        }
    }

    private void RemoveArenaBlock(Player currentPlayer)
    {
        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            String arenaName = SelectedArenaBlocks.get(currentPlayer.getName()).arenaName;
            Block tempBlock = SelectedArenaBlocks.get(currentPlayer.getName()).blockToActivate;
            tempBlock.removeMetadata("ArenaBlock", this);
            currentArenaBlocks.remove(SelectedArenaBlocks.get(currentPlayer
                    .getName()));
            currentPlayer.sendMessage(arenaName + " was removed");
        }
    }

    private void SetWaveTimer(Player currentPlayer, String newPeriod)
    {
        int period = SecondsToTicks(Integer.valueOf(newPeriod));

        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            SelectedArenaBlocks.get(currentPlayer.getName()).timerPeriod = period;
            currentPlayer.sendMessage(SelectedArenaBlocks.get(currentPlayer
                    .getName()).arenaName
                    + " mobs now spawn every "
                    + newPeriod);
        }
    }

    private void SetPlayerBlock(Player currentPlayer)
    {
        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            SelectedArenaBlocks.get(currentPlayer.getName()).LocationToCheckForPlayers = currentPlayer
                    .getLocation();
            currentPlayer.sendMessage(SelectedArenaBlocks.get(currentPlayer
                    .getName()).arenaName + " player block has moved!");
        }
    }

    private void MoveArenaBlock(Player currentPlayer)
    {
        if (SelectedArenaBlocks.get(currentPlayer.getName()) == null)
        {
            currentPlayer
                    .sendMessage("Please select an Arena using /bcarena select <ArenaName>");
            return;
        }
        else
        {
            Block tempBlock = SelectedArenaBlocks.get(currentPlayer.getName()).blockToActivate;
            tempBlock.removeMetadata("ArenaBlock", this);

            tempBlock = currentPlayer.getLocation().getBlock();
            tempBlock.setType(Material.STONE);
            tempBlock.setMetadata("ArenaBlock", new FixedMetadataValue(this,
                    "Something"));
            SelectedArenaBlocks.get(currentPlayer.getName()).blockToActivate = tempBlock;
            currentPlayer.sendMessage(SelectedArenaBlocks.get(currentPlayer
                    .getName()).arenaName + " active block has moved!");
        }
    }

    private void SelectArena(Player currentPlayer, String arenaToSelect)
    {
        ArenaBlock tempArenaBlock = getArenaBlock(arenaToSelect);
        if (tempArenaBlock != null)
        {
            SelectedArenaBlocks.put(currentPlayer.getName(), tempArenaBlock);
            currentPlayer.sendMessage(arenaToSelect + " is now selected");
        }
        else
        {
            currentPlayer.sendMessage("Can't find arena " + arenaToSelect);
        }
    }

    public ArenaBlock getArenaBlock(String arenaToSelect)
    {
        ArenaBlock tempArenaBlock = null;
        for (int i = 0; i < currentArenaBlocks.size(); i++)
        {
            tempArenaBlock = currentArenaBlocks.get(i);
            if (tempArenaBlock != null)
            {
                if (tempArenaBlock.arenaName.contains(arenaToSelect))
                {
                    break;
                }
            }
        }
        return tempArenaBlock;
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

            SelectedArenaBlocks.put(currentPlayer.getName(), newArenaBlock);
            currentArenaBlocks.add(newArenaBlock);

            currentPlayer.sendMessage("Arena " + args[0] + " was created!!");
        }
        catch (NumberFormatException err)
        {
            // currentPlayer.sendMessage("")
        }
    }

    public boolean ArenaExists(String ArenaName)
    {
        boolean tempArenaExists = false;
        if (currentArenaBlocks.size() > 0)
        {
            for (int i = 0; i < currentArenaBlocks.size(); i++)
            {
                if (currentArenaBlocks.get(i).arenaName.equals(ArenaName))
                {
                    tempArenaExists = true;
                    break;
                }
            }
        }
        return tempArenaExists;
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
        SaveArenas();
        this.saveConfig();
    }

    public void SaveArenas()
    {

        for (ArenaBlock ab : currentArenaBlocks)
        {
            String ArenaString = "";
            ArenaString = ArenaString + ab.arenaName + queryStringSep;
            ArenaString = ArenaString + LocationToString(ab.blockToActivate)
                    + queryStringSep;
            ArenaString = ArenaString + LocationToString(ab.arenaWarp)
                    + queryStringSep;
            ArenaString = ArenaString
                    + LocationToString(ab.arenaBlockStartLocation)
                    + queryStringSep;
            ArenaString = ArenaString
                    + LocationToString(ab.LocationToCheckForPlayers)
                    + queryStringToNumberSep;
            ArenaString = ArenaString + ab.radius + queryNumberToStringSep;
            ArenaString = ArenaString + ab.material.name()
                    + queryStringToNumberSep;
            ArenaString = ArenaString + ab.timerDelay + queryNumberSep;
            ArenaString = ArenaString + ab.timerPeriod + queryNumberSep;
            ArenaString = ArenaString + ab.maxRunTimes + queryNumberToStringSep;
            ArenaString = ArenaString
                    + ab.arenaBlockStartLocation.getWorld().getName();
            // ArenaString = ArenaString + "');";

            try
            {

                // String Insert = "CALL arenas.InsertArena('";
                // String Update = "CALL arenas.UpdateArena('";

                String Insert = "InsertArena ";
                String Update = "UpdateArena ";
                // ResultSet arenaExists = sql.query("CALL arenas.ArenaExists('"
                // + ab.arenaName + "');");

                ResultSet arenaExists = sql.query("ArenaExists '"
                        + ab.arenaName + "'");
                
                arenaExists.first();

                if (arenaExists.getString(1) == "0")
                {
                    PreparedStatement ps = sql.prepare(Insert + ArenaString);
                    ps.execute();
                }
                else
                {
                    PreparedStatement ps = sql.prepare(Update + ArenaString);
                    ps.execute();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            getConfig().set("ArenaBlocks", ab.arenaName);

            getConfig().set("ArenaBlocks." + ab.arenaName + ".BlockToActivate",
                    LocationToString(ab.blockToActivate));

            getConfig().set("ArenaBlocks." + ab.arenaName + ".ArenaWarp",
                    LocationToString(ab.arenaWarp));

            getConfig().set(
                    "ArenaBlocks." + ab.arenaName + ".ArenaBlockStartLocation",
                    LocationToString(ab.arenaBlockStartLocation));

            getConfig().set(
                    "ArenaBlocks." + ab.arenaName
                            + ".LocationToCheckForPlayers",
                    LocationToString(ab.LocationToCheckForPlayers));

            getConfig().set("ArenaBlocks." + ab.arenaName + ".Radius",
                    ab.radius);

            getConfig().set("ArenaBlocks." + ab.arenaName + ".Material",
                    ab.material.name());

            getConfig().set("ArenaBlocks." + ab.arenaName + ".TimerDelay",
                    ab.timerDelay);

            getConfig().set("ArenaBlocks." + ab.arenaName + ".TimerPeriod",
                    ab.timerPeriod);

            getConfig().set("ArenaBlocks." + ab.arenaName + ".MaxRunTimes",
                    ab.maxRunTimes);

            getConfig().set("ArenaBlocks." + ab.arenaName + ".World",
                    ab.arenaBlockStartLocation.getWorld().getName());
        }
    }

    private void RecreateArenas()
    {
        String path = "ArenaBlocks";
        FileConfiguration config = this.getConfig();

        Set<String> arenaNames = (config.getConfigurationSection(path) != null ? config
                .getConfigurationSection(path).getKeys(false) : null);

        if (arenaNames == null)
            return;

        for (String an : arenaNames)
        {
            List<String> currentValues = new ArrayList<String>();
            currentValues.add(an);
            path = "ArenaBlocks";
            path = path + "." + an;
            Set<String> ArenaValues = config.getConfigurationSection(path)
                    .getKeys(false);

            for (String av : ArenaValues)
            {
                currentValues.add(config.getString(path + "." + av));
            }

            CreateArena(currentValues);
        }
    }

    public void CreateArena(List<String> currentValues)
    {
        World world = this.getServer().getWorld(currentValues.get(10));
        Block BlockToActivate = StringToLocation(currentValues.get(1), world)
                .getBlock();

        BlockToActivate.setMetadata("ArenaBlock", new FixedMetadataValue(this,
                "Something"));

        ArenaBlock newArenaBlock = new ArenaBlock(BlockToActivate,
                currentValues.get(0), Integer.valueOf(currentValues.get(5)),
                Material.getMaterial(currentValues.get(6)), this,
                Integer.valueOf(currentValues.get(7)),
                Integer.valueOf(currentValues.get(8)));

        newArenaBlock.arenaWarp = StringToLocation(currentValues.get(2), world)
                .getBlock();
        newArenaBlock.maxRunTimes = Integer.valueOf(currentValues.get(9));
        currentArenaBlocks.add(newArenaBlock);
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
