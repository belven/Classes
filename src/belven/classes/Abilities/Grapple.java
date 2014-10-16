package belven.classes.Abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import resources.EntityFunctions;
import belven.arena.arenas.BaseArena.ArenaTypes;
import belven.classes.ClassManager;
import belven.classes.RPGClass;

public class Grapple extends Ability {
	public Grapple(RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		this.currentClass = CurrentClass;
		this.requirements.add(new ItemStack(Material.STRING));
		this.abilitiyName = "Grapple";
	}

	public boolean PerformAbility() {

		List<LivingEntity> tempTargets = EntityFunctions.findAllTargets(currentClass.classOwner, 50.0D);

		if (tempTargets != null && tempTargets.size() > 0) {
			Location l = this.currentClass.classOwner.getLocation();
			l.setY(l.getY() + 1.0D);

			for (LivingEntity le : tempTargets) {
				if (le.getType() == EntityType.PLAYER) {
					Player p = (Player) le;
					if (shouldPull(p)) {
						le.teleport(this.currentClass.classOwner);
					}
				} else {
					le.teleport(this.currentClass.classOwner);
				}
			}

			currentClass.setAbilityOnCoolDown(this);
			RemoveItems();
			return true;
		}
		return false;
	}

	public boolean shouldPull(Player target) {
		Player self = currentClass.classOwner;
		ClassManager plugin = currentClass.plugin;

		if (plugin.arenas != null && plugin.teams != null) {
			boolean selfInArena = plugin.arenas.IsPlayerInArena(self);
			boolean targetInArena = plugin.arenas.IsPlayerInArena(target);
			ArenaTypes arenaType = null;

			if (selfInArena) {
				arenaType = plugin.arenas.getArena(self).type;
			}

			if (selfInArena && targetInArena && arenaType != ArenaTypes.PvP) {
				return true;
			} else if (plugin.teams.isInSameTeam(self, target)) {
				return true;
			}
		}

		return false;
	}
}
