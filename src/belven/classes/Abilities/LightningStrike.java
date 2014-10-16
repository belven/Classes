package belven.classes.Abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class LightningStrike extends Ability {
	public LightningStrike(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		abilitiyName = "Lightning Strike";
	}

	@Override
	public boolean PerformAbility(Entity targetEntity) {
		if (targetEntity instanceof LivingEntity) {
			LivingEntity entityDamaged = (LivingEntity) targetEntity;

			entityDamaged.getWorld().strikeLightning(targetEntity.getLocation());
		}
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}
}
