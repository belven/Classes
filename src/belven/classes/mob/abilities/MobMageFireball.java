package belven.classes.mob.abilities;

import org.bukkit.entity.Fireball;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

public class MobMageFireball extends Ability {
	public MobMageFireball(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "Mage Fireball";
		setShouldBreak(false);
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getOwner().launchProjectile(Fireball.class);
			return true;
		}
		return false;
	}

	@Override
	public int getAmplifier() {
		return Math.round(getRPGClass().getPlayer().getLevel() / 7);
	}
}
