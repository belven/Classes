package belven.classes.timedevents;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.resources.functions;

public class DamageTrapTimer extends BukkitRunnable
{
    private Location blockLocation;
    private Material MaterialToRestore;
    private int trapRadius;
    private int maxDuration;
    private double level;

    public DamageTrapTimer(Block currentBlock, int trapRadius, int level)
    {
        blockLocation = currentBlock.getLocation();
        MaterialToRestore = currentBlock.getType();
        maxDuration = 20;
        this.level = level;
        this.trapRadius = trapRadius;
    }

    public DamageTrapTimer(Block currentBlock, int level)
    {
        this(currentBlock, 3, level);
    }

    @Override
    public void run()
    {
        List<LivingEntity> entitiesToDamage = functions.getNearbyEntities(
                blockLocation, trapRadius);
        maxDuration--;

        if (entitiesToDamage.size() > 0)
        {
            for (LivingEntity e : entitiesToDamage)
            {
                if (e != null)
                {
                    e.damage(level / 5);
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
