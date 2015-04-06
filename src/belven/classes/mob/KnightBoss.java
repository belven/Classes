package belven.classes.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.ClassManager;
import belven.classes.Abilities.Ability;
import belven.mobs.abilities.Cleave;
import belven.mobs.abilities.Pop;
import belven.mobs.abilities.SelfProtection;

public class KnightBoss extends MobClass {

	public KnightBoss(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
		className = "Knight Boss";
		SetAbilities();
		SortAbilities();
	}

	@Override
	public void SelfCast(Player currentPlayer) {

	}

	@Override
	public void SetAbilities() {
		AddAbility(new Pop(this, 2, 1), 5);
		AddAbility(new Cleave(this, 1, 2), 5);
		AddAbility(new SelfProtection(this, 3, 4), 10);
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
		// TODO Auto-generated method stub

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
				if (!a.PerformAbility()) {
					continue;
				} else if (a.shouldBreak) {
					break;
				}
			}
		}
	}
}
