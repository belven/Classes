package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.player.abilities.Bandage;
import belven.classes.player.abilities.Barrier;
import belven.classes.player.abilities.Heal;
import belven.classes.player.abilities.LightHeal;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.MaterialFunctions;

public class Healer extends RPGClass {
	public Heal classHeal;
	public LightHeal classLightHeal;
	public Bandage classBandage;
	public Barrier classBarrier;

	public Healer(int Health, Player currentPlayer, ClassManager instance) {
		super(Health, currentPlayer, instance);
		className = "Healer";
		SetAbilities();
		SetClassDrops();
	}

	public Healer(Player currentPlayer, ClassManager instance) {
		this(8, currentPlayer, instance);
	}

	public synchronized void CheckAbilitiesToCast(Player player, Event event) {
		setTarget(player);
		targetPlayer = player;
		if (MaterialFunctions.isFood(getPlayer().getItemInHand().getType())) {
			return;
		}

		for (Ability a : getAbilities()) {
			if (!a.onCooldown() && a.HasRequirements()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else if (a.shouldBreak()) {
					break;
				}
			}
		}
	}

	@Override
	public void SetClassDrops() {

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		ItemStack lapisBlock = dye.toItemStack(6);

		ItemStack woodSword = new ItemStack(Material.WOOD_SWORD);
		ItemStack stick = new ItemStack(Material.STICK);
		ItemStack paper = new ItemStack(Material.PAPER);

		getClassDrops().add(new ClassDrop(lapisBlock, 30, 1));
		getClassDrops().add(new ClassDrop(woodSword, 1));

		AddChanceToDrop(new ClassDrop(paper, 5, 1), 1);
		AddChanceToDrop(new ClassDrop(stick, 5, 1), 1);

		AddChanceToDrop(new ClassDrop(l_Boots(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_ChestPlate(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_Leggings(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_Helmet(), 1), 1);
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		Player playerSelected;

		if (getPlayer().isSneaking()) {
			CheckAbilitiesToCast(getPlayer(), event);
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(getPlayer(), 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = getPlayer();
			}

			if (getPlugin().isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected, event);
			} else {
				CheckAbilitiesToCast(getPlayer(), event);
			}
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SetAbilities() {
		getAbilities().add(classHeal = new Heal(this, 1, 3));
		getAbilities().add(classLightHeal = new LightHeal(this, 2, 8));
		getAbilities().add(classBandage = new Bandage(this, 0, 3));
		getAbilities().add(classBarrier = new Barrier(this, 6, 4, 10));
		SortAbilities();
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		Player playerSelected;

		if (getPlayer().isSneaking()) {
			CheckAbilitiesToCast(getPlayer(), event);
		} else if (currentEntity.getType() == EntityType.PLAYER) {
			playerSelected = (Player) currentEntity;

			if (getPlugin().isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected, event);
			} else {
				CheckAbilitiesToCast(getPlayer(), event);
			}
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(getPlayer(), 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = getPlayer();
			}

			if (getPlugin().isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected, event);
			} else {
				CheckAbilitiesToCast(getPlayer(), event);
			}
		}
	}
}