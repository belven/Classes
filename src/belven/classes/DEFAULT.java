package belven.classes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
	}

	@Override
	public void SetAbilities() {
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		// TODO Auto-generated method stub

	}
}
