package belven.timedevents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaTimer extends BukkitRunnable
{
    private int radius;
    private List<Block> arenaArea;
    private List<Player> arenaPlayers = new ArrayList<Player>();
    private List<Block> spawnArea = new ArrayList<Block>();
    private Block arenaStartingBlock;
    private Material matToSpawnOn;
    private int mobCounter = 0;
    private int counter = 0;
    private int maxMobCounter = 0;
    private int maxRunTimes = 0;

    public ArenaTimer(Block currentBlock, int areaRadius, Material matToSpawnOn)
    {
        arenaStartingBlock = currentBlock;
        radius = areaRadius;
        this.matToSpawnOn = matToSpawnOn;

        GetArenaArea();
        GetSpawnArea();
    }

    @Override
    public void run()
    {
        if (maxRunTimes >= 5)
        {
            this.cancel();
        }

        GetPlayers();

        if (spawnArea.size() > 0)
        {
            SpawnMobs();
        }

        maxRunTimes++;
    }

    public void GetArenaArea()
    {
        arenaArea = getArenaBlocks(arenaStartingBlock.getLocation(), radius);
    }

    public void GetSpawnArea()
    {
        Location spawnLocation;
        spawnArea.clear();

        if (arenaArea.size() > 0)
        {
            for (int i = 0; i < arenaArea.size(); i++)
            {
                spawnLocation = arenaArea.get(i).getLocation();
                spawnLocation.setY(spawnLocation.getY() - 1);
                spawnLocation = CanSpawnAt(spawnLocation);

                if (spawnLocation != null)
                {
                    Block spawnBlock = spawnLocation.getBlock();
                    spawnArea.add(spawnBlock);
                }
            }
        }
    }

    private Location CanSpawnAt(Location currentLocation)
    {
        Location blockBelow = currentLocation;
        Location blockAbove = currentLocation;
        blockBelow.setY(blockBelow.getY() - 1);
        blockAbove.setY(blockAbove.getY() + 1);
        
        if (currentLocation.getBlock().getType() == Material.AIR
                && blockAbove.getBlock().getType() == Material.AIR
                && blockBelow.getBlock().getType() == matToSpawnOn)
        {
            Bukkit.getServer().getPlayer("belven").sendMessage("Valid Block");
            counter = 0;
            return currentLocation;
        }
        else if (counter >= radius)
        {
            counter = 0;
            return null;
        }
        else
        {
            counter++;
            return CanSpawnAt(blockAbove);
        }
    }

    private void SpawnMobs()
    {
        mobCounter = 0;

        for (int i = 0; i < spawnArea.size(); i++)
        {
            Location spawnLocation = spawnArea.get(i).getLocation();
            spawnLocation.setY(spawnLocation.getY() + 1);
            spawnLocation.getWorld().spawnEntity(spawnLocation,
                    EntityType.ZOMBIE);

            mobCounter++;

            if (mobCounter >= maxMobCounter)
            {
                break;
            }
            else if (i == (spawnArea.size() - 1) && mobCounter < maxMobCounter)
            {
                i = 0;
            }
        }
    }

    public void GetPlayers()
    {
        Player[] currentPlayers = getNearbyPlayers(
                arenaStartingBlock.getLocation(), radius + 3);
        int totalLevels = 0;
        maxMobCounter = 0;

        arenaPlayers.clear();

        if (currentPlayers.length > 0)
        {
            for (int i = 0; i < currentPlayers.length; i++)
            {
                if (currentPlayers[i] instanceof Player)
                {
                    totalLevels += currentPlayers[i].getLevel();
                    arenaPlayers.add(currentPlayers[i]);
                }
            }

            if (arenaPlayers.size() > 0)
            {
                if (totalLevels == 0)
                {
                    maxMobCounter = 5;
                }
                else
                {
                    maxMobCounter = (int) totalLevels / currentPlayers.length;
                }
            }
        }
        else
        {
            maxMobCounter = 0;
        }
    }

    private static Player[] getNearbyPlayers(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();

                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
                        + (chZ * 16)).getChunk().getEntities())
                {
                    if (e.getLocation().distance(l) <= radius
                            && e instanceof Player
                            && e.getLocation().getBlock() != l.getBlock())
                    {
                        radiusEntities.add((Player) e);
                    }
                }
            }
        }

        return radiusEntities.toArray(new Player[radiusEntities.size()]);
    }

    private static List<Block> getArenaBlocks(Location l, int radius)
    {
        World w = l.getWorld();
        int xCoord = (int) l.getX();
        int zCoord = (int) l.getZ();

        List<Block> tempList = new ArrayList<Block>();
        for (int i = 0; i <= 2 * radius; i++)
        {
            for (int j = 0; j <= 2 * radius; j++)
            {
                tempList.add(w.getBlockAt(xCoord + i, l.getBlockY(), zCoord + j));
            }
        }
        return tempList;
    }
}
