package belven.classes.player.abilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

public class LightningStrike extends Ability {
	public LightningStrike(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Lightning Strike";
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget(30, getRPGClass().getPlayer());

		if (targetEntity == null) {
			return false;
		}
		LivingEntity entityDamaged = targetEntity;
		entityDamaged.getWorld().strikeLightning(targetEntity.getLocation());
		getRPGClass().setAbilityOnCoolDown(this);
		return true;
	}
}
