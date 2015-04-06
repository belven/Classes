package belven.classes.player.abilities;

import org.bukkit.entity.LivingEntity;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

public class LightningStrike extends Ability {
	public LightningStrike(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		
		abilitiyName = "Lightning Strike";
	}

	@Override
	public boolean PerformAbility() {
		LivingEntity targetEntity = currentClass.getTarget(30, currentClass.getPlayer());

		if (targetEntity == null) {
			return false;
		}
		LivingEntity entityDamaged = targetEntity;
		entityDamaged.getWorld().strikeLightning(targetEntity.getLocation());
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}
}
