package belven.classes;

import java.util.Iterator;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;
import belven.classes.Abilities.FeelTheBurn;
import belven.classes.Abilities.SetAlight;

public class Daemon extends Berserker
{
    public FeelTheBurn classFeelTheBurn;
    public SetAlight classSetAlight;

    public Daemon(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        this.className = "Daemon";
        classFeelTheBurn = new FeelTheBurn(this);
        classSetAlight = new SetAlight(this);
    }

    public void PerformAbility(Player currentPlayer)
    {
        super.PerformAbility(currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        super.PerformAbility(currentEntity);
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        super.MobTakenDamage(event);

        for (Entity e : functions.getNearbyEntities(event.getEntity()
                .getLocation(), 4))
        {
            e.setFireTicks(classOwner.getFireTicks());
        }
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (!this.classSetAlight.onCooldown
                && this.classSetAlight.HasRequirements(this.classOwner))
        {
            classSetAlight.PerformAbility(classOwner);
            setAbilityOnCoolDown(this.classSetAlight, 1);
        }

        super.TakeDamage(event, damagedPlayer);
    }

    public void PlayerTakenDamage(EntityDamageEvent event)
    {
        Damageable dClassOwner = classOwner;

        double healthPercent = functions.entityCurrentHealthPercent(
                dClassOwner.getHealth(), dClassOwner.getMaxHealth());

        if (event.getCause() == DamageCause.FIRE_TICK
                || event.getCause() == DamageCause.FIRE)
        {
            Iterator<PotionEffect> ActivePotionEffects = classOwner
                    .getActivePotionEffects().iterator();

            while (ActivePotionEffects.hasNext())
            {
                PotionEffect pe = ActivePotionEffects.next();
                if (pe.getType() == PotionEffectType.HUNGER)
                {
                    ActivePotionEffects.remove();
                    break;
                }
            }

            if (healthPercent <= 0.15)
            {
                event.setDamage(0.0);
            }
            else if (!this.classFeelTheBurn.onCooldown
                    && this.classFeelTheBurn.HasRequirements(this.classOwner))
            {
                classFeelTheBurn.PerformAbility(classOwner);
                setAbilityOnCoolDown(this.classFeelTheBurn, 2);
            }

            this.classOwner.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, functions
                            .SecondsToTicks(Amplifier()), 3));
        }
    }

    public int Amplifier()
    {
        return Math.round(classOwner.getLevel() / 5) + 1;
    }
}
