package belven.classes.player.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import belven.classes.Abilities.Ability;
import belven.classes.events.AbilityUsed;
import belven.classes.player.RPGClass;
import belven.resources.EntityFunctions;

public class Retaliation extends Ability {
	public Retaliation(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "Retaliation";
	}

	@Override
	public boolean PerformAbility() {
		Player p = currentClass.getPlayer();
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
		return amplifier;
	}
}