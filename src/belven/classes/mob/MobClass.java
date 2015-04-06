package belven.classes.mob;

import org.bukkit.entity.LivingEntity;

import belven.classes.ClassManager;
import belven.classes.RPGClass;

public abstract class MobClass extends RPGClass {

	public MobClass(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
	}

}
