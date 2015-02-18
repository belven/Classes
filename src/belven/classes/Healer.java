package belven.classes;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
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
		this.SetClassDrops();
	}

	public Healer(Player currentPlayer, ClassManager instance) {
		this(8, currentPlayer, instance);
	}

	public synchronized void CheckAbilitiesToCast(Player player) {
		targetLE = player;
		targetPlayer = player;
		if (MaterialFunctions.isFood(classOwner.getItemInHand().getType())) {
			return;
		}

		for (Ability a : Abilities) {
			if (!a.onCooldown && a.HasRequirements(classOwner)) {
				if (!a.PerformAbility()) {
					continue;
				} else if (a.shouldBreak) {
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

		classDrops.add(new ClassDrop(lapisBlock, true, 20, 1));
		classDrops.add(new ClassDrop(woodSword, true, 1));
		classDrops.add(new ClassDrop(paper, 20, 40, 5, 1));
		classDrops.add(new ClassDrop(stick, 20, 40, 5, 1));

		classDrops.add(new ClassDrop(l_Boots(), 60, 100, 1));
		classDrops.add(new ClassDrop(l_ChestPlate(), 60, 100, 1));
		classDrops.add(new ClassDrop(l_Leggings(), 60, 100, 1));
		classDrops.add(new ClassDrop(l_Helmet(), 60, 100, 1));
	}

	@Override
	public void SelfCast(Player currentPlayer) {
		Player playerSelected;

		if (classOwner.isSneaking()) {
			CheckAbilitiesToCast(classOwner);
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(classOwner, 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = classOwner;
			}

			if (plugin.isAlly(classOwner, playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(classOwner);
			}
		}
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
		Player playerSelected;

		if (classOwner.isSneaking()) {
			CheckAbilitiesToCast(classOwner);
		} else if (currentEntity.getType() == EntityType.PLAYER) {
			playerSelected = (Player) currentEntity;

			if (plugin.isAlly(classOwner, playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(classOwner);
			}
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(classOwner, 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = classOwner;
			}

			if (plugin.isAlly(classOwner, playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(classOwner);
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
	public synchronized void SetAbilities() {
		if (!abilitiesSet) {
			classHeal = new Heal(this, 1, 3);
			classLightHeal = new LightHeal(this, 2, 8);
			classBandage = new Bandage(this, 0, 3);
			classBarrier = new Barrier(this, 6, 4, 10);

			Abilities.add(classBandage);
			Abilities.add(classBarrier);
			Abilities.add(classHeal);
			Abilities.add(classLightHeal);
			SortAbilities();
			abilitiesSet = true;
		}
	}
}