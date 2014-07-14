package belven.classes;

import belven.classes.Abilities.Grapple;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;

public class Berserker extends Class
{
    public Grapple classGrapple;

    public Berserker(Player currentPlayer, ClassManager instance)
    {
        this.plugin = instance;
        this.classOwner = currentPlayer;
        this.className = "Berserker";
        this.classGrapple = new Grapple(this);
        SetAbilities();
        Damageable dcurrentLivingEntity = currentPlayer;
        dcurrentLivingEntity.setMaxHealth(60.0D);
        dcurrentLivingEntity.setHealth(dcurrentLivingEntity.getMaxHealth());
    }

    public void PerformAbility(Player currentPlayer)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        if ((!this.classGrapple.onCooldown)
                && (this.classGrapple.HasRequirements(this.classOwner)))
        {
            List<EntityType> types = new ArrayList<EntityType>();
            types.add(EntityType.SKELETON);
            types.add(EntityType.BLAZE);
            types.add(EntityType.ZOMBIE);

            List<LivingEntity> tempTarget = functions.findTargetEntityByType(
                    this.classOwner, 50.0D, types,
                    this.classOwner.getLevel() / 5 + 1);
            if ((tempTarget != null) && (tempTarget.size() > 0))
            {
                this.classGrapple.PerformAbility(tempTarget);
                setAbilityOnCoolDown(this.classGrapple, 1);
            }
        }
    }

    public void PerformAbility(Entity currentEntity)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        if ((!this.classGrapple.onCooldown)
                && (this.classGrapple.HasRequirements(this.classOwner)))
        {
            this.classGrapple.PerformAbility(currentEntity);
            setAbilityOnCoolDown(this.classGrapple, 2);
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

}
