package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class WebArrow extends Ability {
	public WebArrow(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.SNOW_BALL, 1));
		abilitiyName = "Webbing Arrow";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() != null) {
			getRPGClass().setAbilityOnCoolDown(this);
			getRPGClass().getTarget().addPotionEffect(
					new PotionEffect(PotionEffectType.SLOW, Functions.SecondsToTicks(4), 5));
			RemoveItems();
			return true;
		}

		return false;
	}
}
