package belven.classes.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import belven.classes.ClassManager;
import belven.classes.DEFAULT;
import belven.classes.RPGClass;
import belven.classes.mob.AssassinBoss;
import belven.classes.mob.KnightBoss;
import belven.classes.mob.MageBoss;
import belven.classes.mob.MobClass;
import belven.classes.mob.NecromancerBoss;
import belven.classes.mob.Sapper;
import belven.classes.mob.Warrior;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.Functions;
import belven.resources.Group;
import belven.resources.events.EntityMetadataChanged;

public class MobListener implements Listener {
	private final ClassManager plugin;

	public List<ClassDrop> classDrops = new ArrayList<ClassDrop>();

	Random randomGenerator = new Random();

	public MobListener(ClassManager instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		if (EntityFunctions.IsAMob(event.getEntityType())) {
			LivingEntity le = event.getEntity();
			int rand = new Random().nextInt(99);

			if (Functions.numberBetween(rand, 10, 20)) {
				plugin.SetClass(le, new Warrior(le.getMaxHealth() / 2, le, plugin));
			} else if (Functions.numberBetween(rand, 20, 50)) {
				plugin.SetClass(le, new Sapper(le.getMaxHealth() / 2, le, plugin));
			}
		}
	}

	@EventHandler
	public synchronized void onEntityMetadataChanged(EntityMetadataChanged event) {
		if (event.getEntity() instanceof LivingEntity
				&& (event.getEntity().hasMetadata("ArenaBoss") || event.getEntity().hasMetadata("RewardBoss"))) {
			LivingEntity le = (LivingEntity) event.getEntity();

			int rand = new Random().nextInt(99);
			if (Functions.numberBetween(rand, 0, 25)) {
				plugin.SetClass(le, new KnightBoss(le.getMaxHealth() / 2, le, plugin));
			} else if (Functions.numberBetween(rand, 25, 50)) {
				plugin.SetClass(le, new MageBoss(le.getMaxHealth() / 2, le, plugin));
			} else if (Functions.numberBetween(rand, 50, 75)) {
				plugin.SetClass(le, new NecromancerBoss(le.getMaxHealth() / 2, le, plugin));
			} else {
				plugin.SetClass(le, new AssassinBoss(le.getMaxHealth() / 2, le, plugin));
			}
		}
	}

	@EventHandler
	public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() != null && event.getEntity() instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) event.getEntity();
			if (plugin.GetClass(le) instanceof MobClass) {
				MobClass mc = (MobClass) plugin.GetClass(le);
				mc.SelfTargetOther(event);
			}
		}
	}

	@EventHandler
	public synchronized void onEntityDeathEvent(EntityDeathEvent event) {
		Entity currentEntity = event.getEntity();
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();

		if (currentEntity.getType() != EntityType.PLAYER && currentEntity instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) currentEntity;
			if (!(plugin.GetClass(le) instanceof DEFAULT)) {
				plugin.RemoveClass(le);
			}
		}

		if (lastDamage instanceof EntityDamageByEntityEvent) {
			GiveClassDrops(event, currentEntity, (EntityDamageByEntityEvent) lastDamage);
		}
	}

	private void GiveClassDrops(EntityDeathEvent event, Entity currentEntity, EntityDamageByEntityEvent lastDamage) {
		LivingEntity damager = EntityFunctions.GetDamager(lastDamage);
		Group g = null;

		if (currentEntity.hasMetadata("ArenaMob")) {
			g = (Group) currentEntity.getMetadata("ArenaMob").get(0).value();
		}

		if (g != null && damager != null && damager.getType() == EntityType.PLAYER) {
			String name = g.getName();
			Player damagerP = (Player) damager;

			if (plugin.getGroup(damagerP).getName().equals(name)) {
				for (Player p : plugin.getArenaAllies(damagerP)) {
					GiveClassDrops(p, false);
				}
			}
		} else if (damager != null && damager.getType() == EntityType.PLAYER) {
			Player p = (Player) damager;
			p.giveExp(event.getDroppedExp());
			event.setDroppedExp(0);
			GiveClassDrops(p, true);
		}
	}

	private synchronized void GiveClassDrops(Player p, boolean isWilderness) {
		// int ran = randomGenerator.nextInt(99);
		PlayerInventory pInv = p.getInventory();
		RPGClass playerClass = plugin.GetClass(p);

		for (ClassDrop cd : playerClass.getClassDrops()) {
			if (cd.alwaysGive) {
				if (!cd.isArmor) {
					ItemStack is = cd.is.clone();
					is.setAmount(cd.isWilderness ? cd.wildernessAmount : cd.is.getAmount());
					Functions.AddToInventory(p, is, cd.maxAmount);
				} else if (AddArmor(pInv, cd.is)) {
					break;
				}
			}
		}

		ClassDrop cd = playerClass.getChanceClassDrops().getRandomKey();

		while (cd != null) {
			if (cd.isWilderness == isWilderness) {
				if (!cd.isArmor) {
					ItemStack is = cd.is.clone();
					is.setAmount(cd.isWilderness ? cd.wildernessAmount : cd.is.getAmount());
					Functions.AddToInventory(p, is, cd.maxAmount);
				} else if (!AddArmor(pInv, cd.is)) {
					cd = playerClass.getChanceClassDrops().getRandomKey();
				} else {
					cd = null;
				}
			} else {
				cd = playerClass.getChanceClassDrops().getRandomKey();
			}
		}

		// for (ClassDrop cd : playerClass.getClassDrops()) {
		// if (cd.alwaysGive || Functions.numberBetween(ran, cd.lowChance, cd.highChance)
		// && cd.isWilderness == isWilderness) {
		// if (!cd.isArmor) {
		// ItemStack is = cd.is.clone();
		// is.setAmount(cd.isWilderness ? cd.wildernessAmount : cd.is.getAmount());
		// Functions.AddToInventory(p, is, cd.maxAmount);
		// } else if (AddArmor(pInv, cd.is)) {
		// break;
		// }
		// }
		// }

	}

	public synchronized boolean AddArmor(PlayerInventory pInv, ItemStack is) {
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