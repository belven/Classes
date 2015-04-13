package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.ClassManager;
import belven.classes.player.abilities.AOEHeal;
import belven.classes.player.abilities.Cleanse;
import belven.resources.ClassDrop;

public class Priest extends Healer {

	public Priest(Player currentPlayer, ClassManager instance) {
		super(8, currentPlayer, instance);
		className = "Priest";
		baseClassName = "Healer";
		SetClassDrops();
		SetAbilities();
		SortAbilities();
	}

	@Override
	public synchronized void SetAbilities() {
		super.SetAbilities();
		getAbilities().remove(classHeal);
		getAbilities().remove(classBandage);
		AddAbility(new Cleanse(this, 1, 3), 4);
		AddAbility(new AOEHeal(this, 2, 12), 10);
		classLightHeal.amplifier = 12;
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		ItemStack glow = new ItemStack(Material.GLOWSTONE_DUST, 1);
		getClassDrops().add(new ClassDrop(glow, true, 10));
	}
}