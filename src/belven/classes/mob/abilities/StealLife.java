package belven.classes.mob.abilities;

import org.bukkit.Bukkit;
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
			Bukkit.getServer().getLogger().info("Steal life: " + String.valueOf(amplifier));
			getRPGClass().getTarget().damage(amplifier);
			// EntityFunctions.Heal(getRPGClass().getTarget(), -amplifier);
			EntityFunctions.Heal(getRPGClass().getOwner(), amplifier);
			return true;
		}
		return false;
	}
}
