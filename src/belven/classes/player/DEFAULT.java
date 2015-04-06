package belven.classes.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.ClassManager;

public class DEFAULT extends RPGClass {
	public DEFAULT(Player currentPlayer, ClassManager instance) {
		// Test
		super(10, currentPlayer, instance);
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
