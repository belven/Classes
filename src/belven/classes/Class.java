package belven.classes;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import belven.classes.Abilities.Ability;

public abstract class Class
{
    public ArrayList<Ability> Abilities = new ArrayList<Ability>();
    public Player classOwner = null;
    public ClassManager plugin;
    
    public String getClassName()
    {
        return "";
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (other == this)
        {
            return true;
        }
        else if (!(other instanceof Class))
        {
            return false;
        }
        else
            return false;
    }

    public void PerformAbility(Player currentPlayer)
    {
    }

    public void PerformAbility()
    {
    }

    public void PerformAbility(Entity currentEntity)
    {
        
    }

    public String ListAbilities()
    {
        return null;
    }

    public void AddToAbilities(Ability abilityToAdd)
    {
    }

    public Player classOwner()
    {
        return null;
    }

    public ClassManager plugin()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
