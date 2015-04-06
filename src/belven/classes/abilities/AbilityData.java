package belven.classes.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import belven.classes.RPGClass;

public class AbilityData {
	public List<Event> events = new ArrayList<>();
	protected List<ItemStack> requirements = new ArrayList<ItemStack>();
	protected List<Material> inHandRequirements = new ArrayList<Material>();

	private RPGClass currentClass;

	protected String abilitiyName = "";

	private boolean onCooldown = false;
	private boolean shouldBreak = true;

	public int priority = 0;
	public int amplifier = 5;
	public int cooldown = 1;

	public String GetAbilityName() {
		return abilitiyName;
	}

	public List<ItemStack> GetAbilityRequirements() {
		return requirements;
	}

	public RPGClass getRPGClass() {
		return currentClass;
	}

	public void setRPGClass(RPGClass currentClass) {
		this.currentClass = currentClass;
	}

	public boolean onCooldown() {
		return onCooldown;
	}

	public void setOnCooldown(boolean onCooldown) {
		this.onCooldown = onCooldown;
	}

	public boolean shouldBreak() {
		return shouldBreak;
	}

	public void setShouldBreak(boolean shouldBreak) {
		this.shouldBreak = shouldBreak;
	}

}