package belven.classes.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;

import belven.classes.RPGClass;

public class Ability {
	public List<Event> events = new ArrayList<>();
	protected List<ItemStack> requirements = new ArrayList<ItemStack>();
	protected List<Material> inHandRequirements = new ArrayList<Material>();

	public RPGClass currentClass;
	protected String abilitiyName = "";

	public boolean onCooldown = false;
	public boolean shouldBreak = true;

	public int priority = 0;
	public int amplifier = 5;
	public int cooldown = 1;

	public Ability(RPGClass cc, int Priority, int amplifier) {
		this.priority = Priority;
		this.amplifier = amplifier;
		this.currentClass = cc;
	}

	public Ability(RPGClass cc, int Priority, int amplifier, List<Event> events) {
		this(cc, Priority, amplifier);
		this.events = events;
	}

	public boolean PerformAbility(Event e) {
		return false;
	}

	public int Amplifier() {
		return 0;
	}

	public boolean hasInHandReq() {
		if (inHandRequirements.size() > 0) {
			Material type = getPlayer().getItemInHand().getType();

			if (!inHandRequirements.contains(type)) {
				return false;
			}

			for (ItemStack is : requirements) {
				if (is.getType() == type && hasSameItemData(is, getPlayer().getItemInHand())) {
					return true;
				}
			}

			return false;
		}

		return true;
	}

	public boolean HasRequirements() {
		int checksRequired = 0;
		Inventory playerInventory = getPlayer().getInventory();

		if (!hasInHandReq()) {
			return false;
		}

		for (ItemStack is : requirements) {
			if (playerInventory.containsAtLeast(is, is.getAmount())) {
				for (ItemStack iis : playerInventory) {

					if (iis != null && is != null && iis.getType() == is.getType() && hasSameItemData(is, iis)) {
						checksRequired++;
					}
				}
			}
		}

		if (checksRequired == requirements.size()) {
			return true;
		}
		return false;
	}

	private boolean hasSameItemData(ItemStack is, ItemStack iis) {
		if (is.getType() == Material.INK_SACK) {
			return ((Dye) is.getData()).getColor().equals(((Dye) iis.getData()).getColor());
		} else if (is.getType() == Material.WOOL) {
			return ((Wool) is.getData()).getColor().equals(((Wool) iis.getData()).getColor());
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	public void RemoveItems() {
		Inventory playerInventory = getPlayer().getInventory();

		for (ItemStack is : requirements) {
			for (ItemStack iis : playerInventory) {
				if (iis != null && is != null && iis.getType() == is.getType() && hasSameItemData(is, iis)) {

					if (is.getType() != Material.NETHER_STAR) {

						if (iis.getAmount() > is.getAmount()) {
							iis.setAmount(iis.getAmount() - is.getAmount());
						} else {
							playerInventory.remove(is);
						}
					}
				}
			}
		}
		getPlayer().updateInventory();
	}

	private Player getPlayer() {
		return currentClass.getPlayer();
	}

	public void removeItem(Inventory inv, ItemStack is) {
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null && inv.getItem(i).getType() == is.getType()
					&& hasSameItemData(inv.getItem(i), is)) {
				inv.getItem(i).setType(Material.AIR);
				inv.setItem(i, inv.getItem(i));
			}
		}

	}

	public String GetAbilityName() {
		return abilitiyName;
	}

	public List<ItemStack> GetAbilityRequirements() {
		return requirements;
	}
}
