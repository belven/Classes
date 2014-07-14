package belven.classes;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import resources.functions;
import belven.classes.Abilities.Ability;
import belven.classes.timedevents.AbilityCooldown;

public abstract class Class
{
    public ArrayList<Ability> Abilities = new ArrayList<Ability>();
    public Player classOwner = null;
    public ClassManager plugin;
    protected String className = "";
    public boolean CanCast = true;

    public final String getClassName()
    {
        return className;
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

    public void setAbilityOnCoolDown(Ability currentAbility, int seconds,
            boolean sendMessage)
    {
        new AbilityCooldown(currentAbility, sendMessage).runTaskLater(plugin,
                functions.SecondsToTicks(seconds));
        currentAbility.onCooldown = true;
    }

    public void setAbilityOnCoolDown(Ability currentAbility, int seconds)
    {
        setAbilityOnCoolDown(currentAbility, seconds, false);
    }

    public String ListAbilities()
    {
        return null;
    }

    public void AddToAbilities(Ability abilityToAdd)
    {
    }

    public ClassManager plugin()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void PlayerTakenDamage(EntityDamageEvent event)
    {
        // TODO Auto-generated method stub

    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        // TODO Auto-generated method stub

    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        // TODO Auto-generated method stub

    }

    public void ToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        // TODO Auto-generated method stub
        
    }
}
