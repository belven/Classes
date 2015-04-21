package belven.classes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DEFAULT extends RPGClass {

	public DEFAULT(LivingEntity le, ClassManager instance) {
		super(le, instance);
	}

	public DEFAULT(double health, LivingEntity le, ClassManager instance) {
		super(le, instance);
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
		

	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		

	}
}
