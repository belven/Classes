package belven.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.Abilities.LastResort;

public class Warrior extends Class
{
    public LastResort currentLastResort;

    public Warrior(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        currentLastResort = new LastResort(this);
        className = "Warrior";
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (damagedPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(damagedPlayer, 1))
        {
            currentLastResort.PerformAbility(damagedPlayer);
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);

    }

}
