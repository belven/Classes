package belven.classes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
import belven.timedevents.BarrierTimer;

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
        currentPlayer.setMaxHealth(16);
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;

        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
            return;
        }

        LivingEntity targetEntity = findTarget(classOwner);

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

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;
        
        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
            return;
        }

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            this.CheckAbilitiesToCast(playerSelected);
        }
        else
        {
            LivingEntity targetEntity = findTarget(classOwner);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
                this.CheckAbilitiesToCast(playerSelected);
            }
        }
    }

    @SuppressWarnings("unused")
    public void CheckAbilitiesToCast(Player player)
    {
        if (player.getHealth() <= 10 && classHeal.HasRequirements(classOwner))
        {
            this.classHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
        }
        else if (classBandage.HasRequirements(classOwner))
        {
            this.classBandage.PerformAbility(player);
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
            this.classLightHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
        }
    }

    private LivingEntity findTarget(Player origin)
    {
        double radius = 150.0D;
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        LivingEntity target = null;
        double minDotProduct = Double.MIN_VALUE;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if (entity instanceof Player && !entity.equals(origin))
            {
                LivingEntity living = (LivingEntity) entity;
                Location newTargetLocation = living.getEyeLocation();

                // check angle to target:
                Vector toTarget = newTargetLocation.toVector()
                        .subtract(originVector).normalize();
                double dotProduct = toTarget.dot(originDirection);
                if (dotProduct > 0.30D && origin.hasLineOfSight(living)
                        && (target == null || dotProduct > minDotProduct))
                {
                    target = living;
                    minDotProduct = dotProduct;
                }
            }
        }
        return target;
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            int currentLevel = classOwner.getLevel();

            if (currentLevel > 1)
            {
                this.AddToAbilities(classBandage);
            }

            if (currentLevel > 3)
            {
                this.AddToAbilities(classHeal);
            }
        }
    }

    public String ListAbilities()
    {
        String ListOfAbilities = "";

        if (Abilities != null)
        {
            for (int i = 0; i < Abilities.size(); i++)
            {
                ListOfAbilities = ListOfAbilities
                        + (Abilities.get(i) != null ? Abilities.get(i)
                                .GetAbilityName() + ", " : "");
            }
            return ListOfAbilities;
        }
        else
            return "";
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

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }
}
