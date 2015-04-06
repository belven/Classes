package belven.classes.player.Abilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.player.RPGClass;
import belven.resources.Functions;

public class Stealth extends Ability {
	public Stealth(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		
		abilitiyName = "Stealth";
	}

	@Override
	public boolean PerformAbility() {
		currentClass.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Functions
				.SecondsToTicks(6), 1));
		currentClass.setAbilityOnCoolDown(this, true);
		return true;
	}

	@Override
	public int Amplifier() {
		return 0;
	}
}
