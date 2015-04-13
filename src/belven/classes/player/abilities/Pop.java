package belven.classes.player.abilities;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class Pop extends Ability {
	private int counter = 0;

	public Pop(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		requirements.add(new ItemStack(Material.FEATHER));
		inHandRequirements.add(Material.FEATHER);
		abilitiyName = "Pop";
	}

	@Override
	public boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() == null) {
			return false;
		}

		List<LivingEntity> entitiesToDamage = EntityFunctions.getNearbyEntities(
				getRPGClass().getTarget().getLocation(), 4);

		for (LivingEntity le : entitiesToDamage) {
			if (counter >= 3) {
				counter = 0;
				break;
			} else if (le != null && le.getType() != EntityType.PLAYER) {
				Vector vectorToUse = getRPGClass().getOwner().getLocation().getDirection();
				vectorToUse.setY(vectorToUse.getY() + 1);
				le.setVelocity(vectorToUse);
				counter++;
			}
		}
		RemoveItems();
		return true;
	}
}
