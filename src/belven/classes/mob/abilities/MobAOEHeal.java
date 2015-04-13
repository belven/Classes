package belven.classes.mob.abilities;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.HealTimer;
import belven.resources.EntityFunctions;

public class MobAOEHeal extends Ability {
	public MobAOEHeal(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "AOE Heal";
	}

	@Override
	public boolean PerformAbility(Event e) {
		getRPGClass().setAbilityOnCoolDown(this, true);
		for (LivingEntity le : EntityFunctions.getNearbyEntities(getRPGClass().getOwner().getLocation(), 3)) {
			if (le.getType() != EntityType.PLAYER) {
				new HealTimer(getRPGClass().getPlugin(), getAmplifier() / 100.0, le, 4, 1);
			}
		}
		return true;
	}

}