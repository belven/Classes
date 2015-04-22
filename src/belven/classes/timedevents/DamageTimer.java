package belven.classes.timedevents;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.ClassManager;
import belven.resources.Functions;

public class DamageTimer extends BukkitRunnable {
	private static final String DAMAGE_TIMER = "DamageTimer";
	private Damageable target = null;
	private int DamageTimes = 1;
	private int count = 0;
	private Plugin plugin;
	private double perCentDamage = 0;
	private LivingEntity source = null;

	public DamageTimer(ClassManager cm, double perCentDamage, LivingEntity target, LivingEntity source,
			int DamageTimes, int period) {
		this.target = target;
		this.setSource(source);
		this.DamageTimes = DamageTimes;
		this.plugin = cm;
		this.perCentDamage = perCentDamage;

		if (target.hasMetadata(DAMAGE_TIMER)) {
			DamageTimer other = (DamageTimer) target.getMetadata(DAMAGE_TIMER).get(0).value();
			other.renewDamage(this);
		} else {
			target.setMetadata(DAMAGE_TIMER, new FixedMetadataValue(cm, this));
			this.runTaskTimer(cm, 0, Functions.SecondsToTicks(period));
		}
	}

	public DamageTimer(ClassManager cm, double perCentDamage, LivingEntity target, LivingEntity source) {
		this.target = target;
		this.setSource(source);
		this.runTask(cm);
	}

	public synchronized void renewDamage(DamageTimer dt) {
		int times = Math.round(dt.DamageTimes);
		this.DamageTimes += times;
	}

	@Override
	public void run() {
		if (count < DamageTimes && !target.isDead() && target.isValid()) {
			damage();
		} else {
			if (target.hasMetadata(DAMAGE_TIMER)) {
				target.removeMetadata(DAMAGE_TIMER, plugin);
			}
			this.cancel();
		}
	}

	private void damage() {
		double amountToDamage = target.getMaxHealth() * perCentDamage;
		target.damage(amountToDamage, source);
	}

	public LivingEntity getSource() {
		return source;
	}

	public void setSource(LivingEntity source) {
		this.source = source;
	}
}
