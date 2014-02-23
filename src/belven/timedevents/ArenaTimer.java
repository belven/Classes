package belven.timedevents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.blocks.ArenaBlock;

public class ArenaTimer extends BukkitRunnable
{
    private List<Block> arenaArea;
    private List<Player> arenaPlayers = new ArrayList<Player>();
    private List<Block> spawnArea = new ArrayList<Block>();
    private int mobCounter = 0;
    private int counter = 0;
    private int maxMobCounter = 0;
    private int currentRunTimes = 1;
    private int averageLevel = 0;
    private ArenaBlock arenaBlock;
    private List<EntityType> entitiesToSpawn = new ArrayList<EntityType>();

    public ArenaTimer(ArenaBlock arenaBlock)
    {
        this.arenaBlock = arenaBlock;

        entitiesToSpawn.add(EntityType.ZOMBIE);
        entitiesToSpawn.add(EntityType.SKELETON);

        GetArenaArea();
        GetSpawnArea();
    }

    @Override
    public void run()
    {
        List<Player> currentArenaPlayers = arenaPlayers;

        if (!arenaBlock.isActive)
        {
            currentRunTimes = 1;
            this.cancel();
        }

        if (currentRunTimes >= arenaBlock.maxRunTimes)
        {
            new MessageTimer(currentArenaPlayers, "Arena has ended!!").run();
            arenaBlock.isActive = false;
            currentRunTimes = 1;
            this.cancel();
        }

        GetPlayers();

        if (arenaPlayers.size() <= 0)
        {
            new MessageTimer(currentArenaPlayers, "Arena has ended!!").run();
            arenaBlock.isActive = false;
            currentRunTimes = 0;
            this.cancel();
            return;
        }
        else if (spawnArea.size() > 0 && arenaPlayers.size() > 0)
        {
            // new MessageTimer(currentArenaPlayers,
            // "30 seconds before next wave!!").runTaskLater(
            // arenaBlock.GetPlugin(), arenaBlock.timerPeriod - (30 * 20));
            currentRunTimes++;
            SpawnMobs();
        }
    }

    public void GetArenaArea()
    {
        arenaArea = getArenaBlocks(
                arenaBlock.arenaBlockStartLocation.getLocation(),
                arenaBlock.radius);
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
                spawnLocation = CanSpawnAt(spawnLocation);

                if (spawnLocation != null
                        && !arenaArea.get(i).equals(
                                arenaBlock.arenaBlockStartLocation))
                {
                    Block spawnBlock = spawnLocation.getBlock();
                    spawnArea.add(spawnBlock);
                }
            }
        }
    }

    private Location CanSpawnAt(Location currentLocation)
    {
        Block currentBlock = currentLocation.getBlock();
        Block blockBelow = currentBlock.getRelative(BlockFace.DOWN);
        Block blockAbove = currentBlock.getRelative(BlockFace.UP);

        if (currentBlock.getType() == Material.AIR
                && blockAbove.getType() == Material.AIR
                && blockBelow.getType() == arenaBlock.material)
        {
            counter = 0;
            return currentLocation;
        }
        else if (counter >= arenaBlock.radius)
        {
            counter = 0;
            return null;
        }
        else
        {
            counter++;
            return CanSpawnAt(blockAbove.getLocation());
        }
    }

    private void SpawnMobs()
    {
        mobCounter = 0;
        int heathToscaleTo = (int) (20 + (averageLevel * 1.2));

        new MessageTimer(arenaPlayers, "Mobs Spawning: "
                + String.valueOf(maxMobCounter)).run();

        new MessageTimer(arenaPlayers, "Mobs Health (ish): "
                + String.valueOf(heathToscaleTo)).run();

        if (maxMobCounter > 50)
        {
            maxMobCounter = 50;
        }

        for (mobCounter = 0; mobCounter < maxMobCounter; mobCounter++)
        {
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(spawnArea.size());
            Location spawnLocation = spawnArea.get(randomInt).getLocation();
            spawnLocation.setY(spawnLocation.getY() + 1);
            MobToSpawn(spawnLocation);
        }
    }

    public void MobToSpawn(Location spawnLocation)
    {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(entitiesToSpawn.size());

        LivingEntity currentEntity = (LivingEntity) spawnLocation.getWorld()
                .spawnEntity(spawnLocation, entitiesToSpawn.get(randomInt));
        currentEntity.setCanPickupItems(true);

        if (currentEntity.getType() == EntityType.SKELETON)
        {
            currentEntity.getEquipment().setItemInHand(
                    new ItemStack(Material.BOW));
        }

        int heathToscaleTo = (int) (MobMaxHealth(currentEntity) + (averageLevel * 1.2));

        if (currentEntity.getMaxHealth() != heathToscaleTo)
        {
            currentEntity.setMaxHealth(heathToscaleTo);
            currentEntity.setHealth(heathToscaleTo);
            currentEntity.setMetadata("ArenaMob", new FixedMetadataValue(
                    arenaBlock.GetPlugin(), arenaBlock.playersString));
        }
    }

    public void GetPlayers()
    {
        Location areaToCheck = arenaBlock.LocationToCheckForPlayers;
        Player[] currentPlayers = getNearbyPlayers(areaToCheck,
                arenaBlock.radius + (arenaBlock.radius / 2));
        int totalLevels = 0;
        averageLevel = 0;
        maxMobCounter = 0;
        arenaBlock.playersString = "";
        arenaPlayers.clear();

        if (currentPlayers.length > 0)
        {
            for (int i = 0; i < currentPlayers.length; i++)
            {
                if (currentPlayers[i] instanceof Player)
                {
                    totalLevels += currentPlayers[i].getLevel();
                    arenaPlayers.add(currentPlayers[i]);
                    arenaBlock.playersString = arenaBlock.playersString
                            + currentPlayers[i].getName() + " ";
                }
            }

            if (arenaPlayers.size() > 0)
            {
                if (totalLevels == 0)
                {
                    averageLevel = 1;
                    maxMobCounter = 5;
                }
                else
                {
                    averageLevel = (int) (totalLevels / currentPlayers.length);
                    maxMobCounter = (int) (totalLevels / currentPlayers.length)
                            + (currentPlayers.length * 5);
                }
            }
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

    public int MobMaxHealth(LivingEntity entity)
    {
        if (entity.getType() == EntityType.ZOMBIE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SKELETON)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SPIDER)
        {
            return 16;
        }
        else if (entity.getType() == EntityType.CREEPER)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.WITHER)
        {
            return 300;
        }
        else if (entity.getType() == EntityType.BLAZE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.ENDERMAN)
        {
            return 40;
        }
        else if (entity.getType() == EntityType.CAVE_SPIDER)
        {
            return 12;
        }
        else if (entity.getType() == EntityType.GHAST)
        {
            return 10;
        }
        else if (entity.getType() == EntityType.MAGMA_CUBE)
        {
            MagmaCube MagmaCube = (MagmaCube) entity;

            if (MagmaCube.getSize() == 4)

            {
                return 16;
            }
            else if (MagmaCube.getSize() == 2)
            {
                return 4;
            }
            else
            {
                return 1;
            }
        }
        else if (entity.getType() == EntityType.PIG_ZOMBIE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SLIME)
        {
            Slime slime = (Slime) entity;

            if (slime.getSize() == 4)

            {
                return 16;
            }
            else if (slime.getSize() == 2)
            {
                return 4;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return 20;
        }
    }
}