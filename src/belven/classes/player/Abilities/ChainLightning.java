package belven.classes.player.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;
import belven.classes.Abilities.Ability;
import belven.classes.timedevents.ChainLightningTimer;

public class ChainLightning extends Ability {
	public ChainLightning(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		inHandRequirements.add(Material.NETHER_STAR);
		requirements.add(new ItemStack(Material.NETHER_STAR));
		abilitiyName = "Chain Lightning";
		cooldown = 120;
	}

	@Override
	public boolean PerformAbility() {
		currentClass.getPlayer().sendMessage(abilitiyName + " has been used!");
		new ChainLightningTimer(this).runTaskTimer(currentClass.plugin, 0, 10);
		currentClass.UltAbilityUsed(this);
		return true;
	}
}
