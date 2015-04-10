package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
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
		setShouldBreak(false);
		cooldown = 10;
	}

	@Override
	public boolean PerformAbility(Event e) {
		Player targetPlayer = getRPGClass().targetPlayer;

		if (targetPlayer == null) {
			return false;
		}

		if (EntityFunctions.isHealthLessThanOther(getRPGClass().getPlayer(), targetPlayer)) {
			targetPlayer = getRPGClass().getPlayer();
		}

		if (targetPlayer.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			for (PotionEffect ef : targetPlayer.getActivePotionEffects()) {
				if (ef.getType() == PotionEffectType.ABSORPTION) {

					// This is the sort of thing I'm aiming for
					// if(((Absorption)e).getBonusHealth() > someNumber)
					// etc
				}
			}
		}

		targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Functions.SecondsToTicks(20),
				getAmplifier()), true);

		getRPGClass().setAbilityOnCoolDown(this, true);
		RemoveItems();
		return true;
	}

	@Override
	public int getAmplifier() {
		return Functions.abilityCap((double) amplifier + 1, getRPGClass().getPlayer().getLevel());
	}

}
