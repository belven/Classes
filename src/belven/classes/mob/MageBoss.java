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
import belven.classes.mob.abilities.MobAOEHeal;
import belven.classes.mob.abilities.MobLightningStrike;
import belven.classes.mob.abilities.MobMageFireball;
import belven.resources.EntityFunctions;

public class MageBoss extends MobClass {

	public MageBoss(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Mage Boss";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		AddAbility(new MobMageFireball(this, 1, 1), 1);
		AddAbility(new MobAOEHeal(this, 10, 5), 8);
		AddAbility(new MobLightningStrike(this, 2, 10), 2);
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		if (EntityFunctions.GetDamager(event) == getOwner()) {
			event.setCancelled(true);
			event.setDamage(0);
		} else {
			setTarget(EntityFunctions.GetDamager(event));
		}
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		setTarget((LivingEntity) event.getEntity());
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
		for (Ability a : getAbilities()) {
			if (!a.onCooldown()) {
				if (!a.PerformAbility(null)) {
					continue;
				} else if (a.shouldBreak()) {
					break;
				}
			}
		}
	}
}
