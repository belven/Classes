package belven.classes.mob.abilities;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.EntityFunctions;

public class Cleave extends Ability {

	public Cleave(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		setShouldBreak(true);
	}

	@Override
	public boolean PerformAbility(Event e) {
		List<LivingEntity> entities = EntityFunctions.getNearbyEntities(getRPGClass().getOwner().getLocation(), 4);

		for (LivingEntity le : entities) {
			if (le.getType() == EntityType.PLAYER) {
				EntityFunctions.Heal(le, -amplifier);
			}
		}
		getRPGClass().setAbilityOnCoolDown(this);
		return true;
	}
}
