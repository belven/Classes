package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Wither extends Ability {

	public Wither(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 2;
		abilitiyName = "Wither";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null && getRPGClass().getOwner().hasLineOfSight(getRPGClass().getTarget())) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().addPotionEffect(
					new PotionEffect(PotionEffectType.WITHER, Functions.SecondsToTicks(5), getAmplifier()));
			return true;
		}
		return false;
	}
}
