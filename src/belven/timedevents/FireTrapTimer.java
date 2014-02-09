package belven.timedevents;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class FireTrapTimer extends BukkitRunnable
{
    private Location blockLocation;
    private Material MaterialToRestore;
    private int trapRadius;

    public FireTrapTimer(Block currentBlock, int trapRadius)
    {
        blockLocation = currentBlock.getLocation();
        MaterialToRestore = currentBlock.getType();
        this.trapRadius = trapRadius;
    }

    public FireTrapTimer(Block currentBlock)
    {
        this(currentBlock, 3);
    }

    @Override
    public void run()
    {
        Entity[] entitiesToDamage = getNearbyEntities(blockLocation, trapRadius);

        if (entitiesToDamage.length > 0)
        {
            for (int i = 0; i < entitiesToDamage.length; i++)
            {
                if (entitiesToDamage[i] != null)
                {
                    entitiesToDamage[i].setFireTicks(SecondsToTicks(5));
                }
            }

            blockLocation.getBlock().setType(MaterialToRestore);
            this.cancel();
        }
    }

    private int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

    private static Entity[] getNearbyEntities(Location l, int radius)
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
                            && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}
