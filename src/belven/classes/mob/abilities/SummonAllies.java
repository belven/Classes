package belven.classes.mob.abilities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;

public class SummonAllies extends Ability {

	public SummonAllies(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		abilitiyName = "Summon Allies";
	}

	@Override
	public boolean PerformAbility(Event e) {
		getRPGClass().setAbilityOnCoolDown(this);
		World w = getRPGClass().getOwner().getWorld();
		Location l = getRPGClass().getOwner().getLocation();

		for (int i = 0; i < getAmplifier(); i++) {
			w.spawn(l, Zombie.class);
		}

		return true;
	}
}
