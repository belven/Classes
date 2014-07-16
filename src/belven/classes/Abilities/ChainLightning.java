package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import belven.classes.resources.functions;
import belven.classes.timedevents.ChainLightningTimer;

public class ChainLightning extends Ability
{
    public ChainLightning(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        inHandRequirements.add(Material.NETHER_STAR);
        abilitiyName = "Chain Lightning";
    }

    public boolean PerformAbility(Location targetLocation)
    {
        Entity[] entitiesToDamage = functions.getNearbyEntities(targetLocation,
                12);

        for (Entity e : entitiesToDamage)
        {
            if (e != null && e instanceof LivingEntity
                    && e.getType() != EntityType.PLAYER)
            {
                e.getWorld().strikeLightning(e.getLocation());
            }
        }
        return true;
    }

    @Override
    public boolean PerformAbility()
    {
        new ChainLightningTimer(this).runTaskTimer(currentClass.plugin, 0, 10);
        return true;
    }
}
