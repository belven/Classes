package belven.classes.player.abilities;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class FeelTheBurn extends Ability {
	public FeelTheBurn(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		
		abilitiyName = "Feel The Burn";
	}

	@Override
	public boolean PerformAbility() {
		EntityFunctions.Heal(currentClass.getPlayer(), 2);
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}
}