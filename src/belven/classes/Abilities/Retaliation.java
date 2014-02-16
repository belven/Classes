package belven.classes.Abilities;

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
        if (event.getDamager() instanceof LivingEntity)
        {
            LivingEntity entityDamaged = (LivingEntity) event.getDamager();
            event.setDamage(0);
            entityDamaged.setHealth(entityDamaged.getHealth()
                    - (event.getDamage() * 2));
        }
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }

}
