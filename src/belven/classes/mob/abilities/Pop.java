package belven.classes.mob.abilities;

import org.bukkit.Location;
import org.bukkit.event.Event;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Pop extends Ability {

	public Pop(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		setShouldBreak(true);
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			Location l = getRPGClass().getTarget().getLocation();
			l = Functions.offsetLocation(l, 0, 1, 0);
			getRPGClass().getTarget().teleport(l);
			getRPGClass().setAbilityOnCoolDown(this);
			return true;
		}
		return false;
	}
}
