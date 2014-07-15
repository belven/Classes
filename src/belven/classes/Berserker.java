package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Grapple;
import belvens.classes.resources.ClassDrop;
import belvens.classes.resources.functions;

public class Berserker extends Class
{
    public Grapple classGrapple;

    public Berserker(Player currentPlayer, ClassManager instance)
    {
        this.plugin = instance;
        this.classOwner = currentPlayer;
        this.className = "Berserker";
        this.classGrapple = new Grapple(this, 1);
        SetAbilities();
        Damageable dcurrentLivingEntity = currentPlayer;
        dcurrentLivingEntity.setMaxHealth(60.0D);
        dcurrentLivingEntity.setHealth(dcurrentLivingEntity.getMaxHealth());

        Abilities.add(classGrapple);
        SortAbilities();
        SetClassDrops();
    }

    public void PerformAbility(Player currentPlayer)
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

    public void PerformAbility(Entity currentEntity)
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

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        int mobCount = 0;
        for (Entity e : functions.getNearbyEntities(event.getEntity()
                .getLocation(), 4))
        {
            if ((e instanceof LivingEntity))
            {
                mobCount++;

                if (mobCount >= this.classOwner.getLevel() / 3)
                {
                    break;
                }

                LivingEntity le = (LivingEntity) e;

                if (le == this.classOwner)
                {
                    Damageable dPlayer = this.classOwner;

                    double healthPercent = functions
                            .entityCurrentHealthPercent(dPlayer.getHealth(),
                                    dPlayer.getMaxHealth());
                    if (healthPercent > 0.2D)
                    {
                        le.damage(event.getDamage() / 2.0D);
                    }
                }
                else if (!(le instanceof Player))
                {
                    le.damage(event.getDamage() / 2.0D);
                    le.setLastDamageCause(new EntityDamageEvent(classOwner,
                            DamageCause.ENTITY_ATTACK, event.getDamage() / 2.0));
                }
            }
        }
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        Damageable dPlayer = damagedPlayer;

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

    public void SetAbilities()
    {
    }

    @Override
    public void SetClassDrops()
    {
        ItemStack string = new ItemStack(Material.STRING, 4);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        classDrops.add(new ClassDrop(string, true));
        classDrops.add(new ClassDrop(sword, true));
    }
}
