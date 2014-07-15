package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.Abilities.AOEHeal;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Cleanse;
import belvens.classes.resources.ClassDrop;
import belvens.classes.resources.functions;

public class Priest extends Healer
{
    public AOEHeal classAOEHeal;
    public Cleanse classCleanse;

    public Priest(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        classAOEHeal = new AOEHeal(this, 0);
        classCleanse = new Cleanse(this, 3);
        className = "Priest";
        baseClassName = "Healer";
        Abilities.add(classAOEHeal);
        Abilities.add(classCleanse);
        SortAbilities();
        SetClassDrops();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
        }
        else
        {
            Player playerSelected;
            LivingEntity targetEntity = functions.findTargetPlayer(classOwner,
                    150.0D);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
            }

            CheckAbilitiesToCast(playerSelected);
        }
    }

    public void PerformAbility(Entity currentEntity)
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
        ItemStack glow = new ItemStack(Material.GLOWSTONE_DUST, 1);
        classDrops.add(new ClassDrop(glow, true));
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
                else
                {
                    break;
                }
            }
        }
    }
}
