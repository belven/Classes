package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Heal;

public class Healer extends Class
{  
    public Heal classHeal;
    public Bandage classBandage;

    public Healer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        this.classHeal = new Heal(this);
        this.classBandage = new Bandage(this);
        this.SetAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;
        this.CheckAbilitiesToCast(playerSelected);
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            this.CheckAbilitiesToCast(playerSelected);
        }
        else
        {
            playerSelected = classOwner;
            this.CheckAbilitiesToCast(playerSelected);
        }
    }

    public void CheckAbilitiesToCast(Player player)
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

        if (player.getHealth() <= 10
                && this.classHeal.HasRequirements(classOwner))
        {
            this.classHeal.PerformAbility(player);
            plugin.getServer().broadcastMessage(
                    classOwner().getName() + " healed " + player.getName());
        }
        else if ((classOwner.getItemInHand().getType() == Material.STICK || classOwner
                .getItemInHand().getType() == Material.PAPER)
                && this.classBandage.HasRequirements(classOwner))
        {
            this.classBandage.PerformAbility(player);
            plugin.getServer().broadcastMessage(
                    classOwner().getName() + " gave " + player.getName()
                            + " a bandage");
        }
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

    @Override
    public String getClassName()
    {
        return "Healer";
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

}
