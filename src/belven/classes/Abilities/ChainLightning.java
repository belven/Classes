package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import belven.classes.timedevents.ChainLightningTimer;

public class ChainLightning extends Ability {
	public ChainLightning(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		inHandRequirements.add(Material.NETHER_STAR);
		requirements.add(new ItemStack(Material.NETHER_STAR));
		abilitiyName = "Chain Lightning";
		Cooldown = 120;
	}

	@Override
	public boolean PerformAbility() {
		currentClass.classOwner.sendMessage(abilitiyName + " has been used!");
		new ChainLightningTimer(this).runTaskTimer(currentClass.plugin, 0, 10);
		currentClass.UltAbilityUsed(this);
		return true;
	}
}
