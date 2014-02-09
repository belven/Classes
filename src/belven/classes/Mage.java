package belven.classes;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BlockIterator;

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
        this.classOwner = currentPlayer;
        this.classFireball = new MageFireball(this);
        classChainLightning = new ChainLightning(this);
        classPop = new Pop(this);
        this.SetAbilities();
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
        // if (Abilities != null)
        // {
        // for (int i = 0; i < Abilities.size(); i++)
        // {
        // if (Abilities.get(i).HasRequirements(player))
        // {
        // Abilities.get(i).PerformAbility(player);
        // }
        // }
        // }

        if (classOwner.getItemInHand().getType() == Material.NETHER_STAR
                && classChainLightning.HasRequirements(classOwner))
        {
            classChainLightning.PerformAbility(classOwner.getLocation());
        }
        else if (classOwner.getItemInHand().getType() == Material.FEATHER
                && classPop.HasRequirements(classOwner))
        {
            currentBlockIterator = new BlockIterator(classOwner, 40);

            classPop.PerformAbility(GetTargetBlock().getLocation());
        }
        else if (classFireball.HasRequirements(player))
        {
            this.classFireball.PerformAbility(player);
        }
    }

    public Block GetTargetBlock()
    {
        if (currentBlockIterator != null)
        {
            do
            {
                currentBlockIterator.next();
            }
            while (currentBlockIterator.hasNext() && currentBlockIterator.next().getType() == Material.AIR );
            return currentBlockIterator.next();
        }
        else
            return null;
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

    @Override
    public String getClassName()
    {
        return "Mage";
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
