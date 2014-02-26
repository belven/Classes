package belven.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.Abilities.LastResort;
import belven.classes.Abilities.Retaliation;

public class Warrior extends Class
{
    public LastResort currentLastResort;
    public Retaliation currentRetaliation;

    public Warrior(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        currentLastResort = new LastResort(this);
        currentRetaliation = new Retaliation(this);
        className = "Warrior";
        currentPlayer.setMaxHealth(40);
        currentPlayer.setHealth(currentPlayer.getMaxHealth());
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (!currentLastResort.onCooldown && damagedPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(damagedPlayer))
        {
            UltAbilityUsed(currentLastResort);
            currentLastResort.PerformAbility(damagedPlayer);
        }
        else if (!currentRetaliation.onCooldown && damagedPlayer.isBlocking())
        {
            currentRetaliation.PerformAbility(event);
            setAbilityOnCoolDown(currentRetaliation, 2);
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

}
