package belven.classes.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import belven.classes.ClassManager;
import belven.classes.abilities.Ability;
import belven.classes.mob.abilities.SpeedBoost;
import belven.classes.mob.abilities.StealLife;
import belven.classes.mob.abilities.TeleportToTarget;
import belven.resources.EntityFunctions;

public class AssassinBoss extends MobClass {
	public AssassinBoss(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Assassin Boss";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		AddAbility(new StealLife(this, 10, 4), 2);
		AddAbility(new SpeedBoost(this, 1, 2), 5);
		AddAbility(new TeleportToTarget(this, 1, 2), 10);

	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		setTarget(EntityFunctions.GetDamager(event));
		for (Ability a : getAbilities()) {
			if (!a.onCooldown()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else if (a.shouldBreak()) {
					break;
				}
			}
		}
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		setTarget((LivingEntity) event.getEntity());
		for (Ability a : getAbilities()) {
			if (!a.onCooldown()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else if (a.shouldBreak()) {
					break;
				}
			}
		}
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		// TODO Auto-generated method stub

	}
}
