package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import resources.Functions;

public class SetAlight extends Ability {
	public SetAlight(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		requirements.add(new ItemStack(Material.FIREWORK_CHARGE, 1));
		abilitiyName = "Set Alight";
	}

	@Override
	public boolean PerformAbility() {
		int Amplifier = Amplifier();

		if (Amplifier > 10) {
			Amplifier = 10;
		}

		currentClass.classOwner.setFireTicks(Functions.SecondsToTicks(Amplifier));
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int Amplifier() {
		return Math.round(currentClass.classOwner.getLevel() / 5) + 2;
	}
}
