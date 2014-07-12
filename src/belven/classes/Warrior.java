package belven.classes;

import org.bukkit.entity.Damageable;
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
        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(40.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        Damageable dcurrentPlayer = (Damageable) damagedPlayer;
        if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5
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

}
