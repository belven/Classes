package belven.classes.player.abilities;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class Cleanse extends Ability {
	public Cleanse(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
		abilitiyName = "Cleanse";
		setShouldBreak(true);
		inHandRequirements.add(Material.GLOWSTONE_DUST);
	}

	@Override
	public boolean PerformAbility(Event e) {
		Player playerToHeal = getRPGClass().targetPlayer;

		if (playerToHeal == null) {
			return false;
		}

		playerToHeal.setFireTicks(0);

		Iterator<PotionEffect> ActivePotionEffects = playerToHeal.getActivePotionEffects().iterator();

		while (ActivePotionEffects.hasNext()) {
			PotionEffect pe = ActivePotionEffects.next();
			if (Functions.debuffs(pe.getType())) {
				playerToHeal.removePotionEffect(pe.getType());
			}
		}

		RemoveItems();
		return true;
	}

}
