package belven.classes.mob;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.timedevents.TimedSelfCastTimer;
import belven.resources.Functions;

public abstract class MobClass extends RPGClass {

	public MobClass(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		new TimedSelfCastTimer(this, instance).runTaskTimer(instance, Functions.SecondsToTicks(1),
				Functions.SecondsToTicks(1));
	}

	public abstract void SelfTargetOther(EntityTargetLivingEntityEvent event);

	public abstract void TimedSelfCast();

}
