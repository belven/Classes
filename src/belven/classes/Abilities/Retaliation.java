package belven.classes.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import belven.classes.RPGClass;
import belven.classes.events.AbilityUsed;
import belven.resources.EntityFunctions;

public class Retaliation extends Ability {
	public Retaliation(RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		abilitiyName = "Retaliation";
	}

	@Override
	public boolean PerformAbility() {
		Player p = currentClass.classOwner;
		EntityDamageEvent ldc = p.getLastDamageCause();
		LivingEntity entityDamaged = EntityFunctions.GetDamager(p);

		if (entityDamaged != null) {
			entityDamaged.damage(ldc.getDamage() * Amplifier());
		}

		Bukkit.getPluginManager().callEvent(new AbilityUsed(this));
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int Amplifier() {
		return Amplifier;
	}
}