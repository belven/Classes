package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;

public class Pop extends Ability {

	public Pop(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		setShouldBreak(true);
		abilitiyName = "Pop";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().setVelocity(new Vector(0, 1, 0));
			return true;
		}
		return false;
	}
}
