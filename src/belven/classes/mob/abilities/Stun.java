package belven.classes.mob.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.abilities.Ability;
import belven.classes.mob.MobClass;
import belven.resources.Functions;

public class Stun extends Ability {

	public Stun(MobClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 2;
		abilitiyName = "Stun";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().addPotionEffect(
					new PotionEffect(PotionEffectType.SLOW, Functions.SecondsToTicks(3), 5));
			return true;
		}
		return false;
	}
}
