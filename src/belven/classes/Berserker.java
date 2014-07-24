package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Grapple;
import belven.classes.resources.ClassDrop;
import belven.classes.resources.functions;

public class Berserker extends Class
{
    public Grapple classGrapple;

    public Berserker(Player currentPlayer, ClassManager instance)
    {
        super(30, currentPlayer, instance);
        this.className = "Berserker";
        SetClassDrops();
        SetAbilities();
    }

    @Override
    public void SetClassDrops()
    {
        ItemStack string = new ItemStack(Material.STRING, 4);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        classDrops.add(new ClassDrop(string, true));
        classDrops.add(new ClassDrop(sword, true));
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility())
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
        }
    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility())
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {
        Damageable dPlayer = (Damageable) event.getEntity();

        double healthPercent = 1.3 - functions.entityCurrentHealthPercent(
                dPlayer.getHealth(), dPlayer.getMaxHealth());

        if (healthPercent > 1)
        {
            healthPercent = 1;
        }

        this.classOwner.addPotionEffect(new PotionEffect(
                PotionEffectType.INCREASE_DAMAGE, functions.SecondsToTicks(2),
                (int) (2 * healthPercent)));

        this.classOwner.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                functions.SecondsToTicks(2), (int) (3 * healthPercent)));
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        if (functions.isAMeeleWeapon(classOwner.getItemInHand().getType()))
        {
            event.setDamage(event.getDamage() + 2);
        }

        int mobCount = 0;
        for (Entity e : functions.getNearbyEntities(event.getEntity()
                .getLocation(), 4))
        {
            double damageToDo = event.getDamage() / 3.0D;

            if (e instanceof LivingEntity)
            {
                mobCount++;

                if (mobCount >= this.classOwner.getLevel() / 3)
                {
                    break;
                }

                LivingEntity le = (LivingEntity) e;

                if (le == this.classOwner
                        && !functions.IsAMob(event.getEntityType()))
                {
                    Damageable dPlayer = this.classOwner;

                    double healthPercent = functions
                            .entityCurrentHealthPercent(dPlayer.getHealth(),
                                    dPlayer.getMaxHealth());
                    if (healthPercent > 0.2D)
                    {
                        le.damage(damageToDo);
                    }
                }
                else if (!(le instanceof Player))
                {
                    // HashMap<DamageModifier, Double> damageModifiers = new
                    // HashMap<DamageModifier, Double>();
                    //
                    // EntityDamageEvent ede = new EntityDamageEvent(classOwner,
                    // DamageCause.ENTITY_ATTACK, damageModifiers, null);

                    le.damage(damageToDo);
                    // le.setLastDamageCause(ede);
                }
            }
        }

    }

    @Override
    public void SetAbilities()
    {
        this.classGrapple = new Grapple(this, 1, 0);
        Abilities.add(classGrapple);
        SortAbilities();

    }
}
