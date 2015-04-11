package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Blind extends Ability {

	public Blind(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 2;
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().addPotionEffect(
					new PotionEffect(PotionEffectType.BLINDNESS, Functions.SecondsToTicks(getAmplifier()), 1));
			return true;
		}
		return false;
	}
}
