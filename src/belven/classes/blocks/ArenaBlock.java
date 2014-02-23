package belven.classes.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import belven.classes.ClassManager;
import belven.timedevents.ArenaTimer;

public class ArenaBlock
{
    public String arenaName;
    public Block blockToActivate;
    public Block arenaWarp;
    public Block arenaBlockStartLocation;
    public Location LocationToCheckForPlayers;
    public Integer radius;
    public Material material;
    public int timerDelay;
    public int timerPeriod;
    public String playersString;
    private ClassManager plugin;
    public int maxRunTimes;
    public boolean isActive = false;

    public ArenaBlock(Block block, String ArenaName, Integer radius,
            Material material, ClassManager plugin, int timerDelay,
            int timerPeriod)
    {
        arenaBlockStartLocation = block.getWorld().getBlockAt(
                new Location(block.getWorld(), block.getX(), block.getY() - 1,
                        block.getZ()));
        blockToActivate = block;
        LocationToCheckForPlayers = blockToActivate.getLocation();
        arenaWarp = block;
        this.radius = radius;
        this.material = material;
        this.timerDelay = timerDelay;
        this.timerPeriod = timerPeriod;
        arenaName = ArenaName;
        this.plugin = plugin;
        maxRunTimes = 5;
        // CurrentArenaTimer = new ArenaTimer(this);
    }

    public void Activate()
    {
        isActive = true;
        new ArenaTimer(this).runTaskTimer(plugin, timerDelay, timerPeriod);
    }

    public void Deactivate()
    {
        isActive = false;
        // currentArenaTimer.cancel();
    }

    public ClassManager GetPlugin()
    {
        return plugin;
    }
}
