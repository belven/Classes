package belven.classes.Abilities;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Retaliation extends Ability
{
    public Retaliation(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        abilitiyName = "Retaliation";
    }

    @Override
    public void PerformAbility(EntityDamageByEntityEvent event)
    {
        Entity entityToStrike = event.getDamager();
        if (entityToStrike instanceof LivingEntity)
        {
            LivingEntity entityDamaged = (LivingEntity) event.getDamager();
            entityDamaged.damage(event.getDamage() * 2);
            event.setCancelled(true);
        }
        else
        {
            if (entityToStrike.getType() == EntityType.ARROW)
            {
                Arrow entityArrow = (Arrow) entityToStrike;
                entityToStrike = entityArrow.getShooter();
            }

            LivingEntity entityDamaged = (LivingEntity) entityToStrike;
            entityDamaged.damage(event.getDamage() * 2);
            event.setCancelled(true);
        }
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }

}
