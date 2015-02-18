package belven.classes.timedevents;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.ClassManager;
import belven.resources.Functions;

public class HealTimer extends BukkitRunnable {
	private double perCentHeal = 0.0;
	private Damageable target = null;
	private int HealTimes = 1;
	private int count = 0;
	private Plugin plugin;

	public HealTimer(ClassManager cm, double perCentHeal, LivingEntity target, int HealTimes, int period) {
		this.perCentHeal = perCentHeal;
		this.target = target;
		this.HealTimes = HealTimes;
		this.plugin = cm;

		if (target.hasMetadata("HealTimer")) {
			HealTimer otherHealing = (HealTimer) target.getMetadata("HealTimer").get(0).value();
			otherHealing.renewHealing(this);
		} else {
			target.setMetadata("HealTimer", new FixedMetadataValue(cm, this));
			this.runTaskTimer(cm, 0, Functions.SecondsToTicks(period));
		}
	}

	public synchronized void renewHealing(HealTimer ht) {
		int times = (int) Math.round(ht.HealTimes * 0.4);
		this.HealTimes += times;
	}

	public HealTimer(ClassManager cm, double perCentHeal, LivingEntity target) {
		this.perCentHeal = perCentHeal;
		this.target = target;
		this.runTask(cm);
	}

	@Override
	public void run() {
		if (count < HealTimes) {
			heal();
		} else {
			if (target.hasMetadata("HealTimer")) {
				target.removeMetadata("HealTimer", plugin);
			}
			this.cancel();
		}
	}

	private void heal() {
		double amountToHeal = target.getMaxHealth() * perCentHeal;

		if (target.getHealth() < target.getMaxHealth()) {
			if (amountToHeal + target.getHealth() < target.getMaxHealth()) {
				target.setHealth(amountToHeal + target.getHealth());
			} else {
				target.setHealth(target.getMaxHealth());
			}
			count++;

			// if (target.getType() == EntityType.PLAYER) {
			// ((Player) target).sendMessage("You have been healed: " +
			// String.valueOf(amountToHeal) + " health!");
			// }
		}
	}
}
