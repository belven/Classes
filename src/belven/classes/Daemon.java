package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.FeelTheBurn;
import belven.classes.Abilities.SetAlight;
import belven.classes.resources.ClassDrop;
import belven.classes.resources.functions;

public class Daemon extends Berserker
{
    public FeelTheBurn classFeelTheBurn;
    public SetAlight classSetAlight;

    public Daemon(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        this.className = "Daemon";
        baseClassName = "Berserker";
        classFeelTheBurn = new FeelTheBurn(this, 1);
        classSetAlight = new SetAlight(this, 2);
        SortAbilities();
        SetClassDrops();
    }

    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        super.SelfDamageOther(event);

        for (Entity e : functions.getNearbyEntities(event.getEntity()
                .getLocation(), 4))
        {
            e.setFireTicks(classOwner.getFireTicks());
        }
    }

    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {
        super.SelfTakenDamage(event);
        if (!classSetAlight.onCooldown
                && classSetAlight.HasRequirements(classOwner))
        {
            classSetAlight.PerformAbility(classOwner);
            setAbilityOnCoolDown(classSetAlight, 1);
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageEvent event)
    {
        Damageable dClassOwner = classOwner;

        double healthPercent = functions.entityCurrentHealthPercent(
                dClassOwner.getHealth(), dClassOwner.getMaxHealth());

        if (event.getCause() == DamageCause.FIRE_TICK
                || event.getCause() == DamageCause.FIRE)
        {
            for (PotionEffect pe : classOwner.getActivePotionEffects())
            {
                if (pe.getType() == PotionEffectType.HUNGER)
                {
                    classOwner.addPotionEffect(new PotionEffect(pe.getType(),
                            1, pe.getAmplifier()), true);
                    break;
                }
            }

            if (healthPercent <= 0.15)
            {
                event.setDamage(0.0);
            }
            else if (!classFeelTheBurn.onCooldown
                    && classFeelTheBurn.HasRequirements(classOwner))
            {
                classFeelTheBurn.PerformAbility(classOwner);
                setAbilityOnCoolDown(classFeelTheBurn, 2);
            }

            classOwner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                    functions.SecondsToTicks(Amplifier()), 3));
        }
    }

    public int Amplifier()
    {
        return Math.round(classOwner.getLevel() / 5) + 1;
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        ItemStack fire = new ItemStack(Material.FIREWORK_CHARGE, 2);
        classDrops.add(new ClassDrop(fire, true));
    }
}
