package belven.classes.Abilities;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChainLightning extends Ability
{

    public ChainLightning(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK));
        abilitiyName = "ChainLightning";
    }

    public static Entity[] getNearbyEntities(Location l, int radius)
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
    
    public void PerformAbility(Location targetLocation)
    {
        Entity[] entitiesToDamage = getNearbyEntities(targetLocation, 15);

        for (int i = 0; i < entitiesToDamage.length; i++)
        {
            if (entitiesToDamage[i] != null
                    && entitiesToDamage[i].getType() != EntityType.PLAYER)
            {
                entitiesToDamage[i].getWorld().strikeLightning(
                        entitiesToDamage[i].getLocation());
            }
        }
    }
  
    @Override
    public void PerformAbility()
    {        
    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {        
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }

}
