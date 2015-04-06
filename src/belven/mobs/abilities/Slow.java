package belven.mobs.abilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Slow extends Ability {

	public Slow(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 2;
	}

	@Override
	public boolean PerformAbility() {
		if (currentClass.targetLE != null) {
			currentClass.targetLE.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Functions.SecondsToTicks(3),
					amplifier));
			currentClass.setAbilityOnCoolDown(this);
			return true;
		}
		return false;
	}
}
