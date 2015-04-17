package belven.classes.timedevents;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.RPGClass;
import belven.classes.player.abilities.ChainLightning;
import belven.resources.EntityFunctions;

public class ChainLightningTimer extends BukkitRunnable {
	private RPGClass currentClass;
	private int maxDuration;
	private LivingEntity target;
	private int amp;
	private Location lastLocation;

	public ChainLightningTimer(ChainLightning currentChainLightning) {
		currentClass = currentChainLightning.getRPGClass();
		amp = currentChainLightning.amplifier;
		maxDuration = 24;
	}

	public synchronized void doDamage(LivingEntity le) {
		if (le != null) {
			double damage = ((Damageable) le).getMaxHealth() * (amp * 2 / 100.0);
			le.damage(damage, currentClass.getOwner());
			lastLocation = le.getLocation();
			target = le;
			maxDuration--;
			lastLocation.getWorld().strikeLightningEffect(lastLocation);
		}
	}

	public Location getLocation() {
		return target != null && !target.isDead() ? target.getLocation()
				: currentClass.getPlayer() == null ? lastLocation : currentClass.getPlayer().getLocation();
	}

	@Override
	public void run() {
		if (maxDuration > 0) {
			List<LivingEntity> entities = EntityFunctions.getNearbyEntities(getLocation(), 15);
			for (int i = 0; i < entities.size(); i++) {
				LivingEntity le = entities.get(i);

				if (le != currentClass.getPlayer()) {
					if (le.getType() == EntityType.PLAYER
							&& !currentClass.getPlugin().isAlly(currentClass.getPlayer(), (Player) le)) {
						doDamage(le);
						break;
					} else if (EntityFunctions.IsAMob(le.getType())) {
						doDamage(le);
						break;
					}
				}
			}

		} else {
			currentClass.getPlayer().sendMessage("Chain Lightning Ended");
			this.cancel();
		}
	}
}
