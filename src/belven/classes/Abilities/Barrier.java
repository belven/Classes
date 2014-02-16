package belven.classes.Abilities;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Barrier extends Ability
{
    public int radius = 0;

    public Barrier(belven.classes.Class CurrentClass, int Radius)
    {
        currentClass = CurrentClass;
        radius = Radius;
        inHandRequirements.add(Material.NETHER_STAR);
        abilitiyName = "Barrier";
    }

    public Entity[] getNearbyEntities(Location l)
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
        Entity[] entitiesToDamage = getNearbyEntities(targetLocation);

        for (int i = 0; i < entitiesToDamage.length; i++)
        {
            if (entitiesToDamage[i] != null
                    && entitiesToDamage[i].getType() != EntityType.PLAYER)
            {
                Vector currentVector = entitiesToDamage[i].getVelocity();
                currentVector.setX(currentVector.getX() + 2);
                currentVector.setZ(currentVector.getZ() + 2);
                currentVector.setY(currentVector.getY() + 1);
                entitiesToDamage[i].setVelocity(currentVector);
            }
        }
    }

    @Override
    public void PerformAbility()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int Amplifier()
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
