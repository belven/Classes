package belven.classes.player.abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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

	public boolean PerformAbility(Location targetLocation) {
		List<LivingEntity> entitiesToDamage = EntityFunctions.getNearbyEntities(targetLocation, 4);

		for (Entity e : entitiesToDamage) {
			if (e != null && e.getType() != EntityType.PLAYER) {
				Vector vectorToUse = getRPGClass().getPlayer().getLocation().getDirection();
				vectorToUse.setY(vectorToUse.getY() + 1);
				e.setVelocity(vectorToUse);
				counter++;
				if (counter >= 3) {
					counter = 0;
					break;
				}
			}
		}
		RemoveItems();
		return true;
	}
}
