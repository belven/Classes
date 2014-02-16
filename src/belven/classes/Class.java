package belven.classes;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.Abilities.Ability;
import belven.timedevents.AbilityCooldown;

public abstract class Class
{
    public ArrayList<Ability> Abilities = new ArrayList<Ability>();
    public Player classOwner = null;
    public ClassManager plugin;
    protected String className = "";
    public boolean CanCast = true;

    public final String getClassName()
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

    public void UltAbilityUsed(Ability currentAbility)
    {
        setAbilityOnCoolDown(currentAbility, 120);
    }

    @SuppressWarnings("unused")
    public void setAbilityOnCoolDown(Ability currentAbility, int seconds)
    {
        BukkitTask currentTimer = new AbilityCooldown(currentAbility)
                .runTaskLater(plugin, SecondsToTicks(seconds));
        currentAbility.onCooldown = true;
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

    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }
}
