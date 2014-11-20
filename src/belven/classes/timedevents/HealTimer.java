package belven.classes.timedevents;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import resources.Functions;
import belven.classes.ClassManager;

public class HealTimer extends BukkitRunnable {
	private double perCentHeal = 0.0;
	private Damageable target = null;
	private int HealTimes = 1;
	private int count = 0;

	public HealTimer(ClassManager cm, double perCentHeal, LivingEntity target, int HealTimes, int period) {
		this.perCentHeal = perCentHeal;
		this.target = target;
		this.HealTimes = HealTimes;

		this.runTaskTimer(cm, 0, Functions.SecondsToTicks(period));
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
