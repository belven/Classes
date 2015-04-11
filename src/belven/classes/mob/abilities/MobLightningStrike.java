package belven.classes.mob.abilities;

import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class MobLightningStrike extends Ability {
	public MobLightningStrike(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "Lightning Strike";
		setShouldBreak(false);
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget();

		if (targetEntity == null || getRPGClass().getOwner() != null
				&& !getRPGClass().getOwner().hasLineOfSight(targetEntity)) {
			return false;
		}
		getRPGClass().setAbilityOnCoolDown(this);

		List<LivingEntity> entities = EntityFunctions.getNearbyEntities(targetEntity.getLocation(), 3);
		for (LivingEntity le : entities) {
			if (le.getType() == EntityType.PLAYER) {
				doDamage(le);
			}
		}

		return true;
	}

	public synchronized void doDamage(LivingEntity le) {
		if (le != null) {
			double damage = ((Damageable) le).getMaxHealth() * (amplifier * 2 / 100.0);
			// Functions.callDamageEvent(getRPGClass().getOwner(), le, damage);
			le.damage(damage, getRPGClass().getOwner());
			le.getWorld().strikeLightningEffect(le.getLocation());
		}
	}

}
