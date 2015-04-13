package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.BlockRestorer;
import belven.resources.Functions;

public class Levitate extends Ability {
	private BlockRestorer br;

	public Levitate(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		requirements.add(new ItemStack(Material.FEATHER));
		abilitiyName = "Levitate";
		setShouldBreak(false);
	}

	@Override
	public boolean PerformAbility(Event e) {
		Block below = getRPGClass().getOwner().getLocation().getBlock().getRelative(BlockFace.DOWN);

		if (getTimer() == null && below.getType() == Material.AIR) {
			setTimer(new BlockRestorer(below, Material.WOOL));
			getTimer().runTaskLater(getRPGClass().getPlugin(), Functions.SecondsToTicks(30));
			getRPGClass().getOwner().setVelocity(new Vector().zero());
			RemoveItems();
		}

		return true;
	}

	public BlockRestorer getTimer() {
		return br;
	}

	public void setTimer(BlockRestorer br) {
		this.br = br;
	}
}
