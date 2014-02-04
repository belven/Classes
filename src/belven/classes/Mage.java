package belven.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.MageFireball;

public class Mage extends Class
{
    public List<Ability> Abilities = new ArrayList<Ability>();
    public Player classOwner;
    public ClassManager plugin;
    public MageFireball classFireball;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classFireball = new MageFireball(this);
        SetAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;

        CheckAbilitiesToCast(playerSelected, currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            CheckAbilitiesToCast(playerSelected, classOwner);
        }
        else
        {
            playerSelected = classOwner;
            CheckAbilitiesToCast(playerSelected, classOwner);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        if (Abilities != null)
        {
            for (int i = 0; i < Abilities.size(); i++)
            {
                if (Abilities.get(i).HasRequirements(player))
                {
                    Abilities.get(i).PerformAbility(player);
                }
            }
        }
        
        // if (classFireball.HasRequirements(player))
        // {
        // plugin.getServer().broadcastMessage(
        // classOwner().getName() + " launched a Fireball");
        // classFireball.PerformAbility(player);
        // }

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
}
