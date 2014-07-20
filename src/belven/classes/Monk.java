package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.HealingFurry;
import belven.classes.resources.functions;

public class Monk extends Healer
{
    public HealingFurry classHealingFurry;

    public Monk(Player currentPlayer, ClassManager instance)
    {
        super(12, currentPlayer, instance);
        className = "Monk";
        baseClassName = "Healer";
        SetClassDrops();
        SetAbilities();
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {
        Player playerSelected;

        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
        }
        else
        {
            LivingEntity targetEntity = functions.findTargetPlayer(classOwner,
                    150.0D);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
                CheckAbilitiesToCast(playerSelected);
            }
        }
    }

    @Override
    public void SetAbilities()
    {
        super.SetAbilities();

        Abilities.remove(classHeal);

        classHealingFurry = new HealingFurry(this, 0, 5);

        classHealingFurry.Cooldown = 8;
        classLightHeal.Priority = 10;
        classBandage.Amplifier = 6;
        classLightHeal.Amplifier = 10;

        Abilities.add(classHealingFurry);
        SortAbilities();
    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        Player playerSelected;

        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
        }
        else if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            CheckAbilitiesToCast(playerSelected);
        }
        else
        {
            LivingEntity targetEntity = functions.findTargetPlayer(classOwner,
                    150.0D);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
                CheckAbilitiesToCast(playerSelected);
            }
        }
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        RemoveClassDrop(Material.WOOD_SWORD);
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        if (classOwner.getItemInHand() == null
                || classOwner.getItemInHand().getType() != Material.AIR)
        {
            event.setDamage(event.getDamage() + 6.0);
        }
    }

    public void CheckAbilitiesToCast(Player player)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility(player))
                {
                    continue;
                }
                else if (a.shouldBreak)
                {
                    break;
                }
            }
        }
    }
}
