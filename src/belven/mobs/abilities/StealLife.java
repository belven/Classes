package belven.mobs.abilities;

import belven.classes.Abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.EntityFunctions;

public class StealLife extends Ability {

	public StealLife(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 3;
	}

	@Override
	public boolean PerformAbility() {
		if (currentClass.targetLE != null) {
			EntityFunctions.Heal(currentClass.targetLE, -amplifier);
			EntityFunctions.Heal(currentClass.getPlayer(), amplifier);
			return true;
		}
		return false;
	}
}
