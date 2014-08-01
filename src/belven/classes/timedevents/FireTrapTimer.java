package belven.classes.timedevents;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import resources.EntityFunctions;

public class FireTrapTimer extends BukkitRunnable
{
    private Location blockLocation;
    private Material MaterialToRestore;
    private int trapRadius;
    private int maxDuration;
    private int burnDuration;

    public FireTrapTimer(Block currentBlock, int duartion, int trapRadius)
    {
        blockLocation = currentBlock.getLocation();
        MaterialToRestore = currentBlock.getType();
        maxDuration = 20;
        this.trapRadius = trapRadius;
        burnDuration = duartion;
    }

    public FireTrapTimer(Block currentBlock, int duartion)
    {
        this(currentBlock, duartion, 3);
    }

    @Override
    public void run()
    {
        List<LivingEntity> entitiesToDamage = EntityFunctions.getNearbyEntities(blockLocation,
                trapRadius);
        maxDuration--;

        if (entitiesToDamage.size() > 0)
        {
            for (Entity e : entitiesToDamage)
            {
                if (e != null)
                {
                    e.setFireTicks(burnDuration);
                }
            }

            blockLocation.getBlock().setType(MaterialToRestore);
            this.cancel();
        }
        else if (maxDuration <= 0)
        {
            blockLocation.getBlock().setType(MaterialToRestore);
            this.cancel();
        }
    }

}
