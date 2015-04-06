package belven.classes.timedevents;

import java.util.ArrayList;
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
import belven.resources.Functions;

public class ChainLightningTimer extends BukkitRunnable {
	private RPGClass currentClass;
	private int maxDuration;
	private LivingEntity target;
	private List<LivingEntity> targetsHit = new ArrayList<LivingEntity>();
	private int amp;
	private Location lastLocation;

	public ChainLightningTimer(ChainLightning currentChainLightning) {
		currentClass = currentChainLightning.currentClass;
		amp = currentChainLightning.amplifier;
		maxDuration = 24;
	}

	public synchronized void doDamage(LivingEntity le) {
		if (le != null) {
			double damage = ((Damageable) le).getMaxHealth() * (amp * 2 / 100.0);
			le.damage(damage);
			lastLocation = le.getLocation();
			target = le;
			targetsHit.add(le);
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
			for (LivingEntity e : entities) {
				LivingEntity le = entities.get(Functions.getRandomIndex(entities));

				if (le != currentClass.getPlayer()) {// &&
					// !targetsHit.contains(le))
					// {
					if (le.getType() == EntityType.PLAYER
							&& !currentClass.plugin.isAlly(currentClass.getPlayer(), (Player) le)) {
						doDamage(le);
						break;
					} else if (le.getType() != EntityType.PLAYER) {
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
