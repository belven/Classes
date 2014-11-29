package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import belven.classes.Abilities.HealingFurry;

public class Monk extends Healer {
	public HealingFurry classHealingFurry;

	public Monk(Player currentPlayer, ClassManager instance) {
		super(12, currentPlayer, instance);
		className = "Monk";
		baseClassName = "Healer";
		SetClassDrops();
		SetAbilities();
	}

	@Override
	public void SetAbilities() {
		Abilities.remove(classHeal);

		classHealingFurry = new HealingFurry(this, 0, 5);
		classHealingFurry.Cooldown = 8;
		Abilities.add(classHealingFurry);

		classLightHeal.Priority = 10;
		classLightHeal.Amplifier = 10;

		classBandage.Amplifier = 5;
		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		RemoveClassDrop(Material.WOOD_SWORD);
	}

}