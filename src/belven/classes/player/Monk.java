package belven.classes.player;

import org.bukkit.entity.Player;

import belven.classes.ClassManager;
import belven.classes.player.abilities.BolsterHealth;

public class Monk extends Healer {
	public BolsterHealth classBolsterHealth;

	public Monk(Player currentPlayer, ClassManager instance) {
		super(12, currentPlayer, instance);
		className = "Monk";
		baseClassName = "Healer";
		SetClassDrops();
		SetAbilities();
	}

	@Override
	public synchronized void SetAbilities() {
		super.SetAbilities();
		getAbilities().remove(classHeal);

		classBolsterHealth = new BolsterHealth(this, 0, 5);
		classBolsterHealth.cooldown = 8;
		getAbilities().add(classBolsterHealth);

		classLightHeal.priority = 10;
		classLightHeal.amplifier = 10;

		classBandage.amplifier = 5;
		SortAbilities();
	}

}