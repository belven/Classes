package belven.classes.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import belven.classes.events.AbilityUsed;
import belven.classes.resources.functions;

public class Retaliation extends Ability
{
    public Retaliation(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        abilitiyName = "Retaliation";
    }

    @Override
    public boolean PerformAbility(EntityDamageByEntityEvent event)
    {
        LivingEntity entityDamaged = functions.GetDamager(event);

        if (entityDamaged != null)
        {
            entityDamaged.damage(event.getDamage() * Amplifier());
            event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }

        Bukkit.getPluginManager().callEvent(new AbilityUsed(this));

        return true;
    }

    @Override
    public boolean PerformAbility(EntityDamageEvent event)
    {
        event.setCancelled(true);
        return true;
    }

    @Override
    public int Amplifier()
    {
        return Amplifier;
    }

}
