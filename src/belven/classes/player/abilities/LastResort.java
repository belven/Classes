package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class LastResort extends Ability {
	public LastResort(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		
		requirements.add(new ItemStack(Material.NETHER_STAR, 1));
		abilitiyName = "Last Resort";
	}

	@Override
	public boolean PerformAbility() {
		currentClass.getPlayer().addPotionEffect(
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions.SecondsToTicks(Amplifier()), 4), false);
		return true;
	}

	public int Amplifier() {
		return Math.round(currentClass.getPlayer().getLevel() / amplifier);
	}
}
