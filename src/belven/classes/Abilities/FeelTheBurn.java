package belven.classes.Abilities;

import org.bukkit.entity.Player;

import resources.EntityFunctions;
import resources.Functions;

public class FeelTheBurn extends Ability
{
    public FeelTheBurn(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        abilitiyName = "Feel The Burn";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
    	EntityFunctions.Heal(playerToHeal, 2);
        currentClass.setAbilityOnCoolDown(this);
        return true;
    }

}
