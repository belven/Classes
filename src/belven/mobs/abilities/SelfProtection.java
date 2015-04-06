package belven.mobs.abilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class SelfProtection extends Ability {

	public SelfProtection(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 15;
	}

	@Override
	public boolean PerformAbility() {
		if (currentClass.targetLE != null) {
			currentClass.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions
					.SecondsToTicks(5), amplifier));
			currentClass.setAbilityOnCoolDown(this);
			return true;
		}
		return false;
	}
}
