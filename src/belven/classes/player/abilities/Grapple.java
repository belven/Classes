package belven.classes.player.abilities;

import java.util.List;

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
		List<LivingEntity> tempTargets = EntityFunctions.findAllTargets(getRPGClass().getPlayer(), getAmplifier());

		if (tempTargets != null && tempTargets.size() > 0) {
			for (LivingEntity le : tempTargets) {
				if (shouldPull(le)) {
					le.teleport(this.getRPGClass().getOwner());
					break;
				}
			}

			getRPGClass().setAbilityOnCoolDown(this);
			RemoveItems();
			return true;
		}
		return false;
	}

	public boolean shouldPull(LivingEntity le) {
		if (getRPGClass().getOwner().hasLineOfSight(le)) {
			if (le.getType() == EntityType.PLAYER) {
				Player p = (Player) le;
				if (shouldPullPlayer(p)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public boolean shouldPullPlayer(Player target) {
		return !getRPGClass().getPlugin().isAlly(getRPGClass().getPlayer(), target);
	}
}
