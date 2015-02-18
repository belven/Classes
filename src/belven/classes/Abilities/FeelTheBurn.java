package belven.classes.Abilities;

import belven.resources.EntityFunctions;

public class FeelTheBurn extends Ability {
	public FeelTheBurn(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		abilitiyName = "Feel The Burn";
	}

	@Override
	public boolean PerformAbility() {
		EntityFunctions.Heal(currentClass.classOwner, 2);
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}
}