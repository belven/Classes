package belven.classes;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import resources.functions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
import belven.classes.timedevents.BarrierTimer;

public class Healer extends Class
{
    public Heal classHeal;
    public LightHeal classLightHeal;
    public Bandage classBandage;
    public Barrier classBarrier;

    public Healer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        className = "Healer";
        classHeal = new Heal(this);
        classLightHeal = new LightHeal(this);
        classBandage = new Bandage(this);
        classBarrier = new Barrier(this, 6);
        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(16.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
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
            LivingEntity targetEntity = functions.findTarget(classOwner);

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
            LivingEntity targetEntity = functions.findTarget(classOwner);

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

    @SuppressWarnings("unused")
    public void CheckAbilitiesToCast(Player player)
    {
        Damageable dplayer = (Damageable) player;

        if (dplayer.getHealth() <= 10 && classHeal.HasRequirements(classOwner))
        {
            classHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
        }
        else if (classBandage.HasRequirements(classOwner))
        {
            classBandage.PerformAbility(player);
            classOwner.sendMessage("You gave " + player.getName()
                    + " a bandage");
        }
        else if (!classBarrier.onCooldown
                && classBarrier.HasRequirements(classOwner))
        {
            BukkitTask currentTimer = new BarrierTimer(classBarrier)
                    .runTaskTimer(plugin, 0, 10);

            UltAbilityUsed(classBarrier);

            classOwner.sendMessage("You used " + classBarrier.GetAbilityName());
        }
        else if (classLightHeal.HasRequirements(classOwner))
        {
            classLightHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
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

    @Override
    public Player classOwner()
    {
        return classOwner;
    }
}
