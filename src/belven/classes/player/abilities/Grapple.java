package belven.classes.player.abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class Grapple extends Ability {
	public Grapple(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		this.requirements.add(new ItemStack(Material.STRING));
		this.abilitiyName = "Grapple";
	}

	@Override
	public boolean PerformAbility(Event e) {

		List<LivingEntity> tempTargets = EntityFunctions.findAllTargets(currentClass.getPlayer(), 50.0D);

		if (tempTargets != null && tempTargets.size() > 0) {
			Location l = this.currentClass.getPlayer().getLocation();
			l.setY(l.getY() + 1.0D);

			for (LivingEntity le : tempTargets) {
				if (le.getType() == EntityType.PLAYER) {
					Player p = (Player) le;
					if (shouldPull(p)) {
						le.teleport(this.currentClass.getPlayer());
					}
				} else {
					le.teleport(this.currentClass.getPlayer());
				}
			}

			currentClass.setAbilityOnCoolDown(this);
			RemoveItems();
			return true;
		}
		return false;
	}

	public boolean shouldPull(Player target) {
		// Player self = currentClass.getPlayer();
		// ClassManager plugin = currentClass.plugin;

		// if (plugin.arenas != null && plugin.teams != null) {
		// boolean selfInArena = plugin.arenas.IsPlayerInArena(self);
		// boolean targetInArena = plugin.arenas.IsPlayerInArena(target);
		// ArenaTypes arenaType = null;
		//
		// if (selfInArena) {
		// arenaType = plugin.arenas.getArena(self).getType();
		// }
		//
		// if (selfInArena && targetInArena && arenaType != ArenaTypes.PvP) {
		// return true;
		// } else if (plugin.teams.isInSameTeam(self, target)) {
		// return true;
		// }
		// }

		return true;
	}
}
