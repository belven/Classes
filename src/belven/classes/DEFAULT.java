package belven.classes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DEFAULT extends RPGClass {

	public DEFAULT(double health, LivingEntity le, ClassManager instance) {
		super(health, le, instance);
	}

	public DEFAULT(LivingEntity le, ClassManager classManager) {
		this(le.getMaxHealth() / 2, le, classManager);
	}

	@Override
	public void SetClassDrops() {
	}

	@Override
	public void SelfCast(Player currentPlayer) {
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
	}

	@Override
	public void SetAbilities() {
	}
}
