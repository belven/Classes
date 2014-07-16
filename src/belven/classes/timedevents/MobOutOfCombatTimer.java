package belven.classes.timedevents;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.resources.functions;

public class MobOutOfCombatTimer extends BukkitRunnable
{
    private LivingEntity currentEntity;
    private int healthFromDamage;

    public MobOutOfCombatTimer(LivingEntity CurrentEntity)
    {
        currentEntity = CurrentEntity;
        Damageable dCurrentEntity = (Damageable) CurrentEntity;
        healthFromDamage = (int) dCurrentEntity.getHealth();
    }

    @Override
    public void run()
    {
        Damageable dCurrentEntity = (Damageable) currentEntity;
        if (currentEntity.isDead())
        {
            this.cancel();
        }
        else if (dCurrentEntity.getHealth() == healthFromDamage)
        {
            ResetMaxHealth();
            this.cancel();
        }
    }

    private void ResetMaxHealth()
    {
        currentEntity.setMaxHealth(functions.MobMaxHealth(currentEntity));
    }

}
