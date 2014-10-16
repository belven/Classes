package belven.classes.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import resources.EntityFunctions;
import belven.classes.RPGClass;
import belven.classes.events.AbilityUsed;

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
		LivingEntity entityDamaged = EntityFunctions.GetDamager((LivingEntity) p);

		if (entityDamaged != null) {
			entityDamaged.damage(ldc.getDamage() * Amplifier());
			EntityFunctions.Heal((LivingEntity) p, (int) ldc.getDamage());
		} else {
			EntityFunctions.Heal((LivingEntity) p, (int) ldc.getDamage());
		}
		Bukkit.getPluginManager().callEvent(new AbilityUsed(this));
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public boolean PerformAbility(EntityDamageEvent event) {
		event.setCancelled(true);
		currentClass.setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int Amplifier() {
		return Amplifier;
	}
}