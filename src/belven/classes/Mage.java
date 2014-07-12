package belven.classes;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BlockIterator;

import resources.functions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.ChainLightning;
import belven.classes.Abilities.LightningStrike;
import belven.classes.Abilities.MageFireball;
import belven.classes.Abilities.Pop;

public class Mage extends Class
{
    public MageFireball classFireball;
    public ChainLightning classChainLightning;
    public LightningStrike classLightningStrike;
    public BlockIterator currentBlockIterator;
    public Pop classPop;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classFireball = new MageFireball(this);
        classChainLightning = new ChainLightning(this);
        classLightningStrike = new LightningStrike(this);
        classPop = new Pop(this);
        className = "Mage";
        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(16.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
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
            LivingEntity targetEntity = functions.findTarget(classOwner);

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
            for (Ability a : Abilities)
            {
                ListOfAbilities = ListOfAbilities
                        + (a != null ? a.GetAbilityName() + ", " : "");
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

    @SuppressWarnings("deprecation")
    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (event.getDamager().getType() == EntityType.LIGHTNING)
        {
            event.setDamage(0.0);
        }
        else if (!classLightningStrike.onCooldown)
        {
            Entity entityToStrike = event.getDamager();

            if (entityToStrike.getType() == EntityType.ARROW)
            {
                Arrow entityArrow = (Arrow) entityToStrike;
                entityToStrike = entityArrow.getShooter();
            }

            classLightningStrike.PerformAbility(entityToStrike);
            setAbilityOnCoolDown(classLightningStrike, 2);
        }

    }
}
