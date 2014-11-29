package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.Functions;

public class HealingFurry extends Ability {
	public HealingFurry(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(2));

		currentClass = CurrentClass;
		abilitiyName = "Healing Furry";
	}

	@Override
	public boolean PerformAbility() {
		Player playerToHeal = currentClass.targetPlayer;

		if (playerToHeal == null) {
			return false;
		}

		PotionEffect pe = new PotionEffect(PotionEffectType.HEALTH_BOOST, Functions.SecondsToTicks(Amplifier() + 30),
				Amplifier(), true);

		if (!onCooldown && !playerToHeal.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
			playerToHeal.addPotionEffect(pe, false);

			currentClass.classOwner.sendMessage("You boosted  " + playerToHeal.getName() + "s max health");
			currentClass.setAbilityOnCoolDown(this, true);
			RemoveItems();
			return true;
		}
		return false;
	}

	public boolean containsEffect(PotionEffectType pet, Player playerToHeal) {
		for (PotionEffect pe : playerToHeal.getActivePotionEffects()) {
			if (pe.getType().equals(pet)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap((double) Amplifier + 1, currentClass.classOwner.getLevel());
	}
}