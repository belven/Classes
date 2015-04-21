package belven.classes.player.abilities;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class Cleave extends Ability {

	public Cleave(RPGClass cc, int Priority, int amplifier) {
		super(cc, Priority, amplifier);
		cooldown = 10;
		setShouldBreak(true);
		abilitiyName = "Cleave";
	}

	@Override
	public synchronized boolean PerformAbility(Event e) {
		if (getRPGClass().getTarget() == null) {
			return false;
		}
		int count = 0;
		int total = Math.round(getRPGClass().getPlayer().getLevel() / 5) + 1;

		count++;

		getRPGClass().setAbilityOnCoolDown(this);
		List<LivingEntity> entities = EntityFunctions.getNearbyEntities(getRPGClass().getTarget().getLocation(), 4);

		for (LivingEntity le : entities) {
			if (count < total && shouldAffect(le)) {
				le.damage(amplifier, getRPGClass().getOwner());
				count++;
			}
		}
		return true;
	}

	public boolean shouldAffect(LivingEntity le) {
		if (getRPGClass().getOwner().hasLineOfSight(le)) {
			if (le.getType() == EntityType.PLAYER) {
				Player p = (Player) le;
				if (shouldAffectPlayer(p)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public boolean shouldAffectPlayer(Player target) {
		return !getRPGClass().getPlugin().isAlly(getRPGClass().getPlayer(), target);
	}
}
