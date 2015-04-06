package belven.classes.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import belven.classes.ClassManager;
import belven.classes.abilities.Ability;
import belven.classes.mob.abilities.Cleave;
import belven.classes.mob.abilities.Pop;
import belven.classes.mob.abilities.SelfProtection;

public class KnightBoss extends MobClass {

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
		AddAbility(new Pop(this, 2, 1), 5);
		AddAbility(new Cleave(this, 1, 2), 5);
		AddAbility(new SelfProtection(this, 3, 4), 10);
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		// TODO Auto-generated method stub

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
