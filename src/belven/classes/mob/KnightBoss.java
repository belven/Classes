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
import belven.classes.mob.abilities.Cleave;
import belven.classes.mob.abilities.MobAOEGrapple;
import belven.classes.mob.abilities.SelfProtection;
import belven.classes.mob.abilities.SpeedBoost;
import belven.resources.EntityFunctions;

public class KnightBoss extends MobClass {
	SelfProtection sp;
	private MobAOEGrapple grapple;

	public KnightBoss(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Knight Boss";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		AddAbility(grapple = new MobAOEGrapple(this, 2, 15), 6);
		AddAbility(new Cleave(this, 1, 2), 5);
		AddAbility(new SpeedBoost(this, 1, 1), 2);
		AddAbility(sp = new SelfProtection(this, 3, 4), 7);
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
		

	}

	@Override
	public void SelfTargetOther(EntityTargetLivingEntityEvent event) {
		setTarget(event.getTarget());
	}

	@Override
	public void TimedSelfCast() {
		if (!grapple.onCooldown()) {
			grapple.PerformAbility(null);
		}
	}
}
