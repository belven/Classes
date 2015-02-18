package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.Abilities.AOEHeal;
import belven.classes.Abilities.Cleanse;
import belven.resources.ClassDrop;

public class Priest extends Healer {
	public AOEHeal classAOEHeal;
	public Cleanse classCleanse;

	public Priest(Player currentPlayer, ClassManager instance) {
		super(8, currentPlayer, instance);
		className = "Priest";
		baseClassName = "Healer";
		SetClassDrops();
		SetAbilities();
	}

	@Override
	public synchronized void SetAbilities() {
		super.SetAbilities();

		Abilities.remove(classHeal);

		classAOEHeal = new AOEHeal(this, 0, 12);
		classCleanse = new Cleanse(this, 3, 3);

		classLightHeal.Amplifier = 12;
		classAOEHeal.Cooldown = 8;

		Abilities.add(classAOEHeal);
		Abilities.add(classCleanse);
		Abilities.remove(classBandage);

		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		ItemStack glow = new ItemStack(Material.GLOWSTONE_DUST, 1);
		classDrops.add(new ClassDrop(glow, true, 10));
	}
}