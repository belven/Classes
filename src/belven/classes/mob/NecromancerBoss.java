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
import belven.classes.mob.abilities.Blind;
import belven.classes.mob.abilities.Slow;
import belven.classes.mob.abilities.SummonAllies;
import belven.classes.mob.abilities.Weakness;
import belven.classes.mob.abilities.Wither;

public class NecromancerBoss extends MobClass {

	public NecromancerBoss(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Necromancer Boss";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		AddAbility(new SummonAllies(this, 1, 2), 10);
		AddAbility(new Slow(this, 10, 7), 10);
		AddAbility(new Weakness(this, 2, 4), 5);
		AddAbility(new Blind(this, 2, 5), 10);
		AddAbility(new Wither(this, 2, 10), 10);
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
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
