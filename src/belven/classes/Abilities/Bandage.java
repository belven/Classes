package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.EntityFunctions;
import resources.Functions;

public class Bandage extends Ability {
	public Bandage(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		requirements.add(new ItemStack(Material.PAPER, 1));
		requirements.add(new ItemStack(Material.STICK, 1));
		inHandRequirements.add(Material.PAPER);
		inHandRequirements.add(Material.STICK);
		abilitiyName = "Bandage";
		shouldBreak = false;
		Cooldown = 10;
	}

	@Override
	public boolean PerformAbility() {
		Player targetPlayer = currentClass.targetPlayer;

		if (targetPlayer == null) {
			return false;
		}

		if (EntityFunctions.isHealthLessThanOther(currentClass.classOwner, targetPlayer)) {
			targetPlayer = currentClass.classOwner;
		}

		if (targetPlayer.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			for (PotionEffect e : targetPlayer.getActivePotionEffects()) {
				if (e.getType() == PotionEffectType.ABSORPTION) {

					// This is the sort of thing I'm aiming for
					// if(((Absorption)e).getBonusHealth() > someNumber)
					// etc
				}
			}
		}

		targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Functions.SecondsToTicks(20),
				Amplifier()), true);

		currentClass.setAbilityOnCoolDown(this, true);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap((double) Amplifier + 1, currentClass.classOwner.getLevel());
	}

}
