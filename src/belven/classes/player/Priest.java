package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.ClassManager;
import belven.classes.player.abilities.AOEHeal;
import belven.classes.player.abilities.Cleanse;
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

		getAbilities().remove(classHeal);

		classAOEHeal = new AOEHeal(this, 0, 12);
		classCleanse = new Cleanse(this, 3, 3);

		classLightHeal.amplifier = 12;
		classAOEHeal.cooldown = 8;

		getAbilities().add(classAOEHeal);
		getAbilities().add(classCleanse);
		getAbilities().remove(classBandage);

		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		ItemStack glow = new ItemStack(Material.GLOWSTONE_DUST, 1);
		getClassDrops().add(new ClassDrop(glow, true, 10));
	}
}