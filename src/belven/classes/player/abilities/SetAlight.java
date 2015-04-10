package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class SetAlight extends Ability {
	public SetAlight(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.FIREWORK_CHARGE, 1));
		abilitiyName = "Set Alight";
	}

	@Override
	public boolean PerformAbility(Event e) {
		int Amplifier = getAmplifier();

		if (Amplifier > 10) {
			Amplifier = 10;
		}

		getRPGClass().getPlayer().setFireTicks(Functions.SecondsToTicks(Amplifier));
		getRPGClass().setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int getAmplifier() {
		return Math.round(getRPGClass().getPlayer().getLevel() / 5) + 2;
	}
}
