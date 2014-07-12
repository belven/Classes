package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import resources.functions;
import belven.classes.timedevents.ChainLightningTimer;

public class ChainLightning extends Ability
{
    public ChainLightning(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        inHandRequirements.add(Material.NETHER_STAR);
        abilitiyName = "ChainLightning";
    }
    
    public void PerformAbility(Location targetLocation)
    {
        Entity[] entitiesToDamage = functions.getNearbyEntities(targetLocation, 12);

        for (Entity e : entitiesToDamage)
        {
            if (e != null && e instanceof LivingEntity
                    && e.getType() != EntityType.PLAYER)
            {
                e.getWorld().strikeLightning(e.getLocation());
            }
        }
    }

    @Override
    public void PerformAbility()
    {
        new ChainLightningTimer(this).runTaskTimer(currentClass.plugin, 0, 10);
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
