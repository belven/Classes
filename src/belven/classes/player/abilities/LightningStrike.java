package belven.classes.player.abilities;

import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class LightningStrike extends Ability {
	public LightningStrike(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Lightning Strike";
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget();

		if (targetEntity == null) {
			return false;
		}
		getRPGClass().setAbilityOnCoolDown(this);

		List<LivingEntity> entities = EntityFunctions.getNearbyEntities(targetEntity.getLocation(), 2);
		for (LivingEntity le : entities) {
			if (le != getRPGClass().getPlayer()) {
				if (le.getType() != EntityType.PLAYER || le.getType() == EntityType.PLAYER
						&& !getRPGClass().getPlugin().isAlly(getRPGClass().getPlayer(), (Player) le)) {
					doDamage(le);
				}
			}
		}

		return true;
	}

	public synchronized void doDamage(LivingEntity le) {
		if (le != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			double damage = ((Damageable) le).getMaxHealth() * (amplifier * 2 / 100.0);
			// Functions.callDamageEvent(getRPGClass().getOwner(), le, damage);
			le.damage(damage, getRPGClass().getOwner());
		}
	}

}
