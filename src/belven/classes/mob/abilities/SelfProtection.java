package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class SelfProtection extends Ability {

	public SelfProtection(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().getOwner().addPotionEffect(
					new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions.SecondsToTicks(5), amplifier));
			getRPGClass().setAbilityOnCoolDown(this);
			return true;
		}
		return false;
	}
}
