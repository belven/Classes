package belven.classes.Abilities;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Pop extends Ability {
	private int counter = 0;

	public Pop(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		requirements.add(new ItemStack(Material.FEATHER));
		inHandRequirements.add(Material.FEATHER);
		abilitiyName = "Pop";
	}

	public static Entity[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();

		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();

				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
						radiusEntities.add(e);
				}
			}
		}
		return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}

	public boolean PerformAbility(Location targetLocation) {
		Entity[] entitiesToDamage = getNearbyEntities(targetLocation, 4);

		for (Entity e : entitiesToDamage) {
			if (e != null && e.getType() != EntityType.PLAYER) {
				Vector vectorToUse = currentClass.classOwner.getLocation().getDirection();
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
