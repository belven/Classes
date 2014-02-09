package belven.timedevents;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockRestorer extends BukkitRunnable
{
    private Location blockLocation;
    private Material MaterialToRestore;

    public BlockRestorer(Block currentBlock)
    {
        blockLocation = currentBlock.getLocation();
        MaterialToRestore = currentBlock.getType();
    }

    @Override
    public void run()
    {
        blockLocation.getBlock().setType(MaterialToRestore);
        this.cancel();
    }
}