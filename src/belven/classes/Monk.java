package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import belven.classes.Abilities.BolsterHealth;

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
		Abilities.remove(classHeal);

		classBolsterHealth = new BolsterHealth(this, 0, 5);
		classBolsterHealth.Cooldown = 8;
		Abilities.add(classBolsterHealth);

		classLightHeal.Priority = 10;
		classLightHeal.Amplifier = 10;

		classBandage.Amplifier = 5;
		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		RemoveClassDrop(Material.WOOD_SWORD);
	}

}