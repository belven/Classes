package belven.classes.player.abilities;

import org.bukkit.Material;
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
	public boolean PerformAbility() {
		int Amplifier = Amplifier();

		if (Amplifier > 10) {
			Amplifier = 10;
		}

		currentClass.getPlayer().setFireTicks(Functions.SecondsToTicks(Amplifier));
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int Amplifier() {
		return Math.round(currentClass.getPlayer().getLevel() / 5) + 2;
	}
}
