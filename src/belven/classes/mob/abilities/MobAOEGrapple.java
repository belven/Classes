package belven.classes.mob.abilities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class MobAOEGrapple extends Ability {
	public MobAOEGrapple(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "AOE Grapple";
	}

	@Override
	public boolean PerformAbility(Event e) {
		boolean playerFound = false;
		Location l = getRPGClass().getTarget() != null ? getRPGClass().getTarget().getLocation() : getRPGClass()
				.getOwner().getLocation();

		for (LivingEntity le : EntityFunctions.getNearbyEntities(l, getAmplifier())) {
			if (le.getType() == EntityType.PLAYER && getRPGClass().getOwner().hasLineOfSight(le)) {
				le.teleport(getRPGClass().getOwner());
				playerFound = true;
			}
		}

		if (playerFound) {
			getRPGClass().setAbilityOnCoolDown(this);
			return true;
		}
		return false;
	}
}