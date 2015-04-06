package belven.classes.player.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.Abilities.Ability;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public class Bandage extends Ability {
	public Bandage(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.PAPER, 1));
		requirements.add(new ItemStack(Material.STICK, 1));
		inHandRequirements.add(Material.PAPER);
		inHandRequirements.add(Material.STICK);
		abilitiyName = "Bandage";
		shouldBreak = false;
		cooldown = 10;
	}

	@Override
	public boolean PerformAbility() {
		Player targetPlayer = currentClass.targetPlayer;

		if (targetPlayer == null) {
			return false;
		}

		if (EntityFunctions.isHealthLessThanOther(currentClass.getPlayer(), targetPlayer)) {
			targetPlayer = currentClass.getPlayer();
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
		return Functions.abilityCap((double) amplifier + 1, currentClass.getPlayer().getLevel());
	}

}
