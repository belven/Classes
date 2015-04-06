package belven.classes.mob.abilities;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.EntityFunctions;

public class Cleave extends Ability {

	public Cleave(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		shouldBreak = true;
	}

	@Override
	public boolean PerformAbility() {
		List<LivingEntity> entities = EntityFunctions.getNearbyEntities(currentClass.getPlayer().getLocation(), 2);

		for (LivingEntity le : entities) {
			if (le.getType() == EntityType.PLAYER) {
				EntityFunctions.Heal(le, -amplifier);
			}
		}
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}
}
