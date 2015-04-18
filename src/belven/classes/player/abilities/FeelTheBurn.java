package belven.classes.player.abilities;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public class FeelTheBurn extends Ability {
	public FeelTheBurn(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Feel The Burn";
	}

	@Override
	public boolean PerformAbility(Event e) {
		getRPGClass().setAbilityOnCoolDown(this);
		getRPGClass().getOwner().addPotionEffect(
				new PotionEffect(PotionEffectType.SPEED, Functions.SecondsToTicks(getAmplifier()), 3));
		EntityFunctions.Heal(getRPGClass().getOwner(), getAmplifier());
		return true;
	}
}