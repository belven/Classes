package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import resources.functions;

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

    public void PerformAbility(Location targetLocation)
    {
        Entity[] entitiesToDamage = functions.getNearbyEntities(targetLocation,
                radius);

        for (Entity e : entitiesToDamage)
        {
            if (e != null && e.getType() != EntityType.PLAYER)
            {
                Vector currentVector = e.getVelocity();
                currentVector.setX(currentVector.getX() + 1);
                currentVector.setZ(currentVector.getZ() + 1);
                currentVector.setY(currentVector.getY() + 1);
                e.setVelocity(currentVector);
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
