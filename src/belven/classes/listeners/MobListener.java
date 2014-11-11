package belven.classes.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import resources.ClassDrop;
import resources.EntityFunctions;
import resources.Functions;
import resources.Group;
import belven.classes.ClassManager;

public class MobListener implements Listener {
	private final ClassManager plugin;

	public List<ClassDrop> classDrops = new ArrayList<ClassDrop>();

	Random randomGenerator = new Random();

	public MobListener(ClassManager instance) {
		plugin = instance;
	}

	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		// if (event.getEntity().getType() == EntityType.FIREBALL) {
		// event.setCancelled(true);
		// }
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		Entity currentEntity = event.getEntity();
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();

		if (!(lastDamage instanceof EntityDamageByEntityEvent)) {
			return;
		}

		Entity damager = EntityFunctions.GetDamager((EntityDamageByEntityEvent) lastDamage);
		Object data = null;

		if (currentEntity.hasMetadata("ArenaMob")) {
			data = currentEntity.getMetadata("ArenaMob").get(0).value();
		}

		if (data != null && damager.getType() == EntityType.PLAYER) {
			Group g = (Group) data;
			String name = g.getName();
			Player damagerP = (Player) damager;

			if (plugin.getGroup(damagerP).getName().equals(name)) {
				for (Player p : plugin.getArenaAllies(damagerP)) {
					GiveClassDrops(p, false);
				}
			}
		} else {
			EntityDamageEvent cause = event.getEntity().getLastDamageCause();

			if (cause instanceof EntityDamageByEntityEvent) {
				LivingEntity e = EntityFunctions.GetDamager((EntityDamageByEntityEvent) cause);

				if (e.getType() == EntityType.PLAYER) {
					Player p = (Player) e;

					p.giveExp(event.getDroppedExp());
					event.setDroppedExp(0);
					GiveClassDrops(p, true);
				}
			}
		}
	}

	private void GiveClassDrops(Player p, boolean isWilderness) {
		int ran = randomGenerator.nextInt(99);
		PlayerInventory pInv = p.getInventory();
		belven.classes.RPGClass playerClass = plugin.GetClass(p);

		for (ClassDrop cd : playerClass.classDrops) {
			if (cd.alwaysGive || Functions.numberBetween(ran, cd.lowChance, cd.highChance)
					&& cd.isWilderness == isWilderness) {
				if (!cd.isArmor) {
					ItemStack is = cd.is.clone();
					is.setAmount(cd.isWilderness ? cd.wildernessAmount : cd.is.getAmount());
					Functions.AddToInventory(p, is, cd.maxAmount);
				} else {
					if (AddArmor(pInv, cd.is)) {
						break;
					}
				}
			}
		}

	}

	public boolean AddArmor(PlayerInventory pInv, ItemStack is) {
		if (is.getType().name().contains("CHEST") && pInv.getChestplate() == null) {
			pInv.setChestplate(is);
			return true;
		} else if (is.getType().name().contains("LEGGINGS") && pInv.getLeggings() == null) {
			pInv.setLeggings(is);
			return true;
		} else if (is.getType().name().contains("HELMET") && pInv.getHelmet() == null) {
			pInv.setHelmet(is);
			return true;
		} else if (is.getType().name().contains("BOOTS") && pInv.getBoots() == null) {
			pInv.setBoots(is);
			return true;
		}
		return false;
	}
}