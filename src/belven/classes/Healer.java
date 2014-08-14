package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import resources.EntityFunctions;
import resources.MaterialFunctions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
import belven.classes.resources.ClassDrop;

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

	public void CheckAbilitiesToCast(Player player) {
		if (MaterialFunctions.isFood(classOwner.getItemInHand().getType())) {
			return;
		}

		for (Ability a : Abilities) {
			if (!a.onCooldown && a.HasRequirements(classOwner)) {
				if (!a.PerformAbility(player)) {
					continue;
				} else if (a.shouldBreak) {
					break;
				}
			}
		}
	}

	@Override
	public void SetClassDrops() {
		ItemStack lapisBlock = new ItemStack(Material.LAPIS_BLOCK, 6);
		ItemStack woodSword = new ItemStack(Material.WOOD_SWORD);
		ItemStack stick = new ItemStack(Material.STICK);
		ItemStack paper = new ItemStack(Material.PAPER);

		classDrops.add(new ClassDrop(lapisBlock, true, 10, 1));
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
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(
					classOwner, 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = classOwner;
			}

			if (shouldHeal(classOwner, playerSelected)) {
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

			if (shouldHeal(classOwner, playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(classOwner);
			}
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(
					classOwner, 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = classOwner;
			}

			if (shouldHeal(classOwner, playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(classOwner);
			}
		}
	}

	public boolean shouldHeal(Player self, Player target) {
		if (plugin.arenas != null && plugin.teams != null) {
			boolean selfInArena = plugin.arenas.IsPlayerInArena(self);
			boolean targetInArena = plugin.arenas.IsPlayerInArena(target);

			if (selfInArena && targetInArena) {
				return true;
			} else if (plugin.teams.isInSameTeam(self, target)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SetAbilities() {
		classHeal = new Heal(this, 1, 3);
		classLightHeal = new LightHeal(this, 2, 3);
		classBandage = new Bandage(this, 0, 3);
		classBarrier = new Barrier(this, 6, 4, 10);

		Abilities.add(classBandage);
		Abilities.add(classBarrier);
		Abilities.add(classHeal);
		Abilities.add(classLightHeal);
		SortAbilities();
	}
}