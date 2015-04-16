package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Weakness extends Ability {

	public Weakness(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 2;
		abilitiyName = "Weakness";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null && getRPGClass().getOwner().hasLineOfSight(getRPGClass().getTarget())) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().addPotionEffect(
					new PotionEffect(PotionEffectType.WEAKNESS, Functions.SecondsToTicks(3), getAmplifier()));
			return true;
		}
		return false;
	}
}
