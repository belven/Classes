package belven.classes.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import belven.classes.ClassManager;
import belven.classes.abilities.Ability;
import belven.classes.mob.abilities.Slow;
import belven.classes.mob.abilities.StealLife;
import belven.classes.mob.abilities.Weakness;

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
		AddAbility(new StealLife(this, 10, 2), 4);
		AddAbility(new Slow(this, 1, 3), 5);
		AddAbility(new Weakness(this, 1, 3), 10);
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		

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
		

	}

	@Override
	public void SelfTargetOther(EntityTargetLivingEntityEvent event) {
		

	}

	@Override
	public void TimedSelfCast() {
		

	}
}
