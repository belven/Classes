package belven.classes.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import belven.classes.ClassManager;
import belven.classes.abilities.Ability;
import belven.classes.mob.abilities.Slow;
import belven.classes.mob.abilities.StealLife;

public class Sapper extends MobClass {

	public Sapper(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Sapper";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		abilities.add(new StealLife(this, 10, 1));
		abilities.add(new Slow(this, 1, 1));
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		targetLE = (LivingEntity) event.getEntity();
		for (Ability a : abilities) {
			if (!a.onCooldown) {
				if (!a.PerformAbility(event)) {
					continue;
				} else if (a.shouldBreak) {
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
