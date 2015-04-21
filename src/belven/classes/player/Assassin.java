package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.player.abilities.Bleed;
import belven.classes.player.abilities.SoulDrain;
import belven.classes.player.abilities.Stealth;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;

public class Assassin extends RPGClass {
	private SoulDrain classSoulDrain;
	private Stealth classStealth;
	private Bleed bleed;

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
		Block b = locationToTeleportTo.getBlock();
		Block below = b.getRelative(BlockFace.DOWN);

		if (b.getType() == Material.AIR && below.getType() != Material.AIR) {
			Location temp = EntityFunctions.lookAt(locationToTeleportTo, mobLocation);
			getOwner().teleport(temp);
		} else {
			Location temp = EntityFunctions.lookAt(mobLocation, mobLocation);
			getOwner().teleport(temp);
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

		Dye dye = new Dye();
		dye.setColor(DyeColor.RED);

		getClassDrops().add(new ClassDrop(sword, 1));

		AddChanceToDrop(new ClassDrop(bow, 1), 1.0);
		AddChanceToDrop(new ClassDrop(arrow, 30), 1.0);
		AddChanceToDrop(new ClassDrop(speed, 1), 1.0);
		AddChanceToDrop(new ClassDrop(dye.toItemStack(3), 10), 1.0);
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
		setTarget((LivingEntity) damagedEntity);

		boolean arrowEntity = event.getDamager().getType() == EntityType.ARROW;

		if (!arrowEntity) {
			if (!bleed.onCooldown() && bleed.HasRequirements()) {
				bleed.PerformAbility(event);
			}

			if (!classStealth.onCooldown()) {
				classStealth.PerformAbility(event);
			}

			EntityFunctions.Heal(getOwner(), 1);
		} else {
			TeleportToTarget(damagedEntity);
		}
	}

	@Override
	public void SetAbilities() {
		AddAbility(classSoulDrain = new SoulDrain(this, 1, 0), 3);
		AddAbility(classStealth = new Stealth(this, 1, 5), 10);
		AddAbility(bleed = new Bleed(this, 1, 2), 2);
		SortAbilities();
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		if (event.getRightClicked() instanceof LivingEntity) {
			setTarget((LivingEntity) event.getRightClicked());
			if (!classSoulDrain.onCooldown() && classSoulDrain.HasRequirements()) {
				classSoulDrain.PerformAbility(event);
			}
		}
	}
}
