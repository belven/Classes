package belven.classes;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import resources.functions;
import belven.classes.Abilities.AOEHeal;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Cleanse;

public class Priest extends Healer
{
    public AOEHeal classAOEHeal;
    public Cleanse classCleanse;

    public Priest(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        classAOEHeal = new AOEHeal(this);
        classCleanse = new Cleanse(this);
        className = "Priest";
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

    public void CheckAbilitiesToCast(Player player)
    {
        Damageable dplayer = (Damageable) player;
        Damageable dclassOwner = (Damageable) classOwner;

        if (dclassOwner.getHealth() < dplayer.getHealth())
        {
            dplayer = dclassOwner;
        }

        if (!classAOEHeal.onCooldown
                && classAOEHeal.HasRequirements(classOwner))
        {
            classAOEHeal.PerformAbility(player);
            setAbilityOnCoolDown(classAOEHeal, 8);
        }
        else
        {
            super.CheckAbilitiesToCast(player);
        }

        if (!classCleanse.onCooldown
                && classCleanse.HasRequirements(classOwner))
        {
            classCleanse.PerformAbility(player);
        }
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            int currentLevel = classOwner.getLevel();

            if (currentLevel > 1)
            {
                AddToAbilities(classBandage);
            }

            if (currentLevel > 3)
            {
                AddToAbilities(classHeal);
            }
        }
    }

    public void AddToAbilities(Ability abilityToAdd)
    {
        Abilities.add(abilityToAdd);
    }

    @Override
    public void PerformAbility()
    {

    }

}
