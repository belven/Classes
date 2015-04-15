package belven.classes.player.abilities;

import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class FeelTheBurn extends Ability {
	public FeelTheBurn(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Feel The Burn";
	}

	@Override
	public boolean PerformAbility(Event e) {
		EntityFunctions.Heal(getRPGClass().getPlayer(), 4);
		getRPGClass().setAbilityOnCoolDown(this);
		RemoveItems();
		return true;
	}
}