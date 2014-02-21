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
    public Block arenaBlockStartLocation;
    public Location LocationToCheckForPlayers;
    private Integer radius;
    private Material material;
    private int timerDelay;
    public int timerPeriod;
    private ClassManager plugin;
    public boolean isActive = false;

    public ArenaBlock(Block block, String ArenaName, Integer radius, Material material,
            ClassManager plugin, int timerDelay, int timerPeriod)
    {
        arenaBlockStartLocation = block;
        blockToActivate = block;
        LocationToCheckForPlayers = blockToActivate.getLocation();
        this.radius = radius;
        this.material = material;
        this.timerDelay = timerDelay;
        this.timerPeriod = timerPeriod;
        arenaName = ArenaName;

        this.plugin = plugin;
    }

    public void Activate()
    {
        isActive = true;
        Block tempblock = arenaBlockStartLocation.getWorld().getBlockAt(
                new Location(arenaBlockStartLocation.getWorld(),
                        arenaBlockStartLocation.getX(), arenaBlockStartLocation
                                .getY() - 1, arenaBlockStartLocation.getZ()));
        
        new ArenaTimer(this, tempblock, radius, material, LocationToCheckForPlayers).runTaskTimer(plugin,
                timerDelay, timerPeriod);
    }

    public ClassManager GetPlugin()
    {
        return plugin;
    }
}
