package belven.classes.Abilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.Functions;

public class Stealth extends Ability {
	public Stealth(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		abilitiyName = "Stealth";
	}

	@Override
	public boolean PerformAbility() {
		currentClass.classOwner.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Functions
				.SecondsToTicks(6), 1));
		currentClass.classOwner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions
				.SecondsToTicks(6), 5));
		currentClass.setAbilityOnCoolDown(this, true);
		return true;
	}

	@Override
	public int Amplifier() {
		return 0;
	}
}
