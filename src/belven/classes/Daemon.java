package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.EntityFunctions;
import resources.Functions;
import belven.classes.Abilities.FeelTheBurn;
import belven.classes.Abilities.SetAlight;
import belven.classes.resources.ClassDrop;

public class Daemon extends Berserker
{
    public FeelTheBurn classFeelTheBurn;
    public SetAlight classSetAlight;

    public Daemon(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        this.className = "Daemon";
        baseClassName = "Berserker";
        SetClassDrops();
        SetAbilities();
    }

    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        super.SelfDamageOther(event);

        for (Entity e : EntityFunctions.getNearbyEntities(event.getEntity()
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
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageEvent event)
    {
        double healthPercent = plugin.GetPlayerE(classOwner).GetHealthPercent();

        if (event.getCause() == DamageCause.FIRE_TICK
                || event.getCause() == DamageCause.FIRE)
        {

            if (classOwner.hasPotionEffect(PotionEffectType.HUNGER))
            {
                classOwner.removePotionEffect(PotionEffectType.HUNGER);
            }

            if (healthPercent <= 0.15)
            {
                event.setDamage(0.0);
            }
            else if (!classFeelTheBurn.onCooldown
                    && classFeelTheBurn.HasRequirements(classOwner))
            {
                classFeelTheBurn.PerformAbility(classOwner);
            }

            classOwner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                    Functions.SecondsToTicks(Amplifier()), 3));
        }
    }

    public int Amplifier()
    {
        return Functions.abilityCap(5, classOwner.getLevel()) + 1;
    }

    @Override
    public void SetAbilities()
    {
        super.SetAbilities();
        classFeelTheBurn = new FeelTheBurn(this, 1, 0);
        classSetAlight = new SetAlight(this, 2, 0);
        classSetAlight.Cooldown = 1;
        classFeelTheBurn.Cooldown = 2;
        SortAbilities();
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        ItemStack fire = new ItemStack(Material.FIREWORK_CHARGE, 2);
        classDrops.add(new ClassDrop(fire, true, 10));
    }
}
