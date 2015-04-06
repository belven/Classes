package belven.classes.player.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;
import belven.classes.Abilities.Ability;

public class WebArrow extends Ability {
	public WebArrow(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		requirements.add(new ItemStack(Material.SNOW_BALL, 1));
		
		abilitiyName = "Webbing Arrow";
	}
}
