package belven.classes.mob.abilities;

import org.bukkit.event.Event;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;

public class TeleportToTarget extends Ability {

	public TeleportToTarget(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 3;
		abilitiyName = "Teleport To Target";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getOwner().teleport(getRPGClass().getTarget());
			return true;
		}
		return false;
	}
}
