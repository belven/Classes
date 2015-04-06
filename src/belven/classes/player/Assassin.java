package belven.classes.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.player.abilities.SoulDrain;
import belven.classes.player.abilities.Stealth;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;

public class Assassin extends RPGClass {
	private SoulDrain classSoulDrain;
	private Stealth classStealth;

	public Assassin(Player currentPlayer, ClassManager instance) {
		super(12, currentPlayer, instance);
		className = "Assassin";
		SetAbilities();
		SetClassDrops();
	}

	public void TeleportToTarget(Entity currentEntity) {
		Location mobLocation = currentEntity.getLocation(), locationToTeleportTo = currentEntity.getLocation();
		Location playerLocation = getOwner().getLocation(), recallLocation = getOwner().getLocation();

		recallLocation.setY(recallLocation.getY() - 1);
		// recallBlock = recallLocation.getBlock();

		if (HasLineOfSight(currentEntity)) {
			if (playerLocation.getZ() < mobLocation.getZ()) {
				locationToTeleportTo.setZ(locationToTeleportTo.getZ() + 1);
			} else {
				locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
			}

			if (playerLocation.getX() < mobLocation.getX()) {
				locationToTeleportTo.setX(locationToTeleportTo.getX() + 1);
			} else {
				locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
			}
		} else {
			locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
			locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
		}

		CanTeleportTo(locationToTeleportTo, mobLocation);
	}

	public boolean HasLineOfSight(Entity damagedEntity) {
		if (damagedEntity instanceof LivingEntity) {
			LivingEntity currentLivingEntity = (LivingEntity) damagedEntity;
			return currentLivingEntity.hasLineOfSight(getOwner());
		}
		return false;
	}

	public void CanTeleportTo(Location locationToTeleportTo, Location mobLocation) {
		for (int i = (int) locationToTeleportTo.getY(); i < locationToTeleportTo.getY() + 20; i++) {
			if (locationToTeleportTo.getBlock().getType() == Material.AIR) {
				Location temp = EntityFunctions.lookAt(locationToTeleportTo, mobLocation);
				getOwner().teleport(temp);
				break;
			} else {
				locationToTeleportTo.setY(i);
			}
		}
	}

	@Override
	public void ToggleSneakEvent(PlayerToggleSneakEvent event) {
		// if (event.isSneaking() && recallBlock != null) {
		// Location recallLocation = recallBlock.getLocation();
		// recallLocation.setY(recallLocation.getY() + 1);
		// classOwner.teleport(recallLocation);
		// recallBlock = null;
		// }
	}

	@Override
	public void SetClassDrops() {
		ItemStack arrow = new ItemStack(Material.ARROW, 3);
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack speed = new Potion(PotionType.SPEED, 2).toItemStack(1);

		classDrops.add(new ClassDrop(bow, true, 1));
		classDrops.add(new ClassDrop(sword, true, 1));
		classDrops.add(new ClassDrop(arrow, true, 30));
		classDrops.add(new ClassDrop(speed, 0, 100, 1));
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {

	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		Entity damagedEntity = event.getEntity();
		boolean arrowEntity = event.getDamager().getType() == EntityType.ARROW;

		if (getOwner().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			getOwner().removePotionEffect(PotionEffectType.INVISIBILITY);
		}

		if (!arrowEntity) {
			if (!classStealth.onCooldown()) {
				classStealth.PerformAbility(event);
			}
		} else {
			TeleportToTarget(damagedEntity);
		}

		EntityFunctions.Heal(getOwner(), 1);
	}

	@Override
	public void SetAbilities() {
		if (!abilitiesSet) {
			classStealth = new Stealth(this, 1, 1);
			classSoulDrain = new SoulDrain(this, 1, 0);
			getAbilities().add(classSoulDrain);
			getAbilities().add(classStealth);
			classStealth.cooldown = 3;
			SortAbilities();
			abilitiesSet = true;
		}
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		if (!classSoulDrain.onCooldown() && getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
			classSoulDrain.PerformAbility(event);
			UltAbilityUsed(classSoulDrain);
		}
	}
}
