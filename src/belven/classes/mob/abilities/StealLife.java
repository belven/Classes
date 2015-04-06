package belven.classes.mob.abilities;

import org.bukkit.event.Event;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.EntityFunctions;

public class StealLife extends Ability {

	public StealLife(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 3;
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			EntityFunctions.Heal(getRPGClass().getTarget(), -amplifier);
			EntityFunctions.Heal(getRPGClass().getOwner(), amplifier);
			return true;
		}
		return false;
	}
}
