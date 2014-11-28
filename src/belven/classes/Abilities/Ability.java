package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;

public class Ability {
	List<ItemStack> requirements = new ArrayList<ItemStack>();
	List<Material> inHandRequirements = new ArrayList<Material>();
	public belven.classes.RPGClass currentClass;
	protected String abilitiyName = "";
	public boolean onCooldown = false;
	public boolean shouldBreak = true;
	public int Priority = 0;
	public int Amplifier = 5;
	public int Cooldown = 0;

	public Ability(int Priority, int amplifier) {
		this.Priority = Priority;
		Amplifier = amplifier;
	}

	public boolean PerformAbility() {
		return false;
	}

	// public boolean PerformAbility(Player targetPlayer) {
	// return false;
	// }
	//
	// public boolean PerformAbility(Entity targetEntity) {
	// return false;
	// }

	public int Amplifier() {
		return 0;
	}

	public boolean hasInHandReq(Player p) {
		if (inHandRequirements.size() > 0) {
			Material type = p.getItemInHand().getType();

			if (!inHandRequirements.contains(type)) {
				return false;
			}

			for (ItemStack is : requirements) {
				if (is.getType() == type && hasSameItemData(is, p.getItemInHand())) {
					return true;
				}
			}

			return false;
		}

		return true;
	}

	public boolean HasRequirements(Player p) {
		int checksRequired = 0;
		Inventory playerInventory = p.getInventory();

		if (!hasInHandReq(p)) {
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
		Inventory playerInventory = currentClass.classOwner.getInventory();

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
		currentClass.classOwner.updateInventory();
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

	// public boolean PerformAbility(EntityDamageByEntityEvent event) {
	// return false;
	// }
	//
	// public boolean PerformAbility(EntityDamageEvent event) {
	// return false;
	// }
	//
	// public boolean PerformAbility(Location location) {
	// return false;
	// }
}
