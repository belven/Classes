package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import belven.classes.resources.functions;
import belven.classes.timedevents.BarrierTimer;

public class Barrier extends Ability
{
    public int radius = 0;

    public Barrier(belven.classes.Class CurrentClass, int Radius, int priority,
            int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        radius = Radius;
        inHandRequirements.add(Material.NETHER_STAR);
        abilitiyName = "Barrier";
    }

    public boolean PerformAbility(Location targetLocation)
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

        return true;
    }

    @Override
    public boolean PerformAbility()
    {
        new BarrierTimer(this).runTaskTimer(currentClass.plugin, 0, 10);
        currentClass.UltAbilityUsed(this);
        currentClass.classOwner.sendMessage("You used " + GetAbilityName());
        return true;
    }
}
