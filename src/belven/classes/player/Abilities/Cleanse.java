package belven.classes.player.Abilities;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import belven.classes.RPGClass;
import belven.classes.Abilities.Ability;
import belven.resources.Functions;

public class Cleanse extends Ability {
	public Cleanse(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		
		requirements.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
		abilitiyName = "Cleanse";
	}

	@Override
	public boolean PerformAbility() {
		Player playerToHeal = currentClass.targetPlayer;

		if (playerToHeal == null) {
			return false;
		}

		playerToHeal.setFireTicks(0);

		Iterator<PotionEffect> ActivePotionEffects = playerToHeal.getActivePotionEffects().iterator();

		while (ActivePotionEffects.hasNext()) {
			PotionEffect pe = ActivePotionEffects.next();
			if (Functions.debuffs(pe.getType())) {
				playerToHeal.removePotionEffect(pe.getType());
				break;
			}
		}

		RemoveItems();
		return true;
	}

}
