package belven.classes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.ChainLightning;
import belven.classes.Abilities.MageFireball;
import belven.classes.Abilities.Pop;

public class Mage extends Class
{
    public MageFireball classFireball;
    public ChainLightning classChainLightning;
    public BlockIterator currentBlockIterator;
    public Pop classPop;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classFireball = new MageFireball(this);
        classChainLightning = new ChainLightning(this);
        classPop = new Pop(this);
        className = "Mage";
        currentPlayer.setMaxHealth(16);
        SetAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;

        this.CheckAbilitiesToCast(playerSelected, currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            this.CheckAbilitiesToCast(playerSelected, classOwner);
        }
        else
        {
            playerSelected = classOwner;
            this.CheckAbilitiesToCast(playerSelected, classOwner);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        if (!classChainLightning.onCooldown
               && classChainLightning.HasRequirements(classOwner))
        {
            classChainLightning.PerformAbility();

            UltAbilityUsed(classChainLightning);
        }
        else if (classPop.HasRequirements(classOwner))
        {
            LivingEntity targetEntity = findTarget(classOwner);

            if (targetEntity != null)
            {
                classPop.PerformAbility(targetEntity.getLocation());
            }
        }
        else if (classFireball.HasRequirements(player))
        {
            this.classFireball.PerformAbility(player);
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
            if (entity instanceof LivingEntity && !entity.equals(origin))
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
                AddToAbilities(new MageFireball(this));
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
        if (Abilities != null)
        {
            Abilities.add(abilityToAdd);
        }
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

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (event.getDamager().getType() == EntityType.LIGHTNING)
        {
            event.setDamage(0);
        }

    }
}
