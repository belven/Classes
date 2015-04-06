package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import belven.classes.ClassManager;
import belven.classes.player.Abilities.BolsterHealth;

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
		abilities.remove(classHeal);

		classBolsterHealth = new BolsterHealth(this, 0, 5);
		classBolsterHealth.cooldown = 8;
		abilities.add(classBolsterHealth);

		classLightHeal.priority = 10;
		classLightHeal.amplifier = 10;

		classBandage.amplifier = 5;
		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		RemoveClassDrop(Material.WOOD_SWORD);
	}

}