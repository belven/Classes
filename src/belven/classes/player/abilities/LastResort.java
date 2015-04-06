package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.HealTimer;

public class LastResort extends Ability {
	public LastResort(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.NETHER_STAR, 1));
		abilitiyName = "Last Resort";
	}

	@Override
	public boolean PerformAbility(Event e) {
		new HealTimer(getRPGClass().getPlugin(), Amplifier() / 100.0, getRPGClass().getOwner());
		getRPGClass().UltAbilityUsed(this);
		return true;
	}

}
