package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		this.SetClassDrops();
	}

	public Healer(Player currentPlayer, ClassManager instance) {
		this(8, currentPlayer, instance);
	}

	public synchronized void CheckAbilitiesToCast(Player player) {
		targetLE = player;
		targetPlayer = player;
		if (MaterialFunctions.isFood(getPlayer().getItemInHand().getType())) {
			return;
		}

		for (Ability a : abilities) {
			if (!a.onCooldown && a.HasRequirements()) {
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

		if (getPlayer().isSneaking()) {
			CheckAbilitiesToCast(getPlayer());
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(getPlayer(), 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = getPlayer();
			}

			if (plugin.isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(getPlayer());
			}
		}
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
		Player playerSelected;

		if (getPlayer().isSneaking()) {
			CheckAbilitiesToCast(getPlayer());
		} else if (currentEntity.getType() == EntityType.PLAYER) {
			playerSelected = (Player) currentEntity;

			if (plugin.isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(getPlayer());
			}
		} else {
			LivingEntity targetEntity = EntityFunctions.findTargetPlayer(getPlayer(), 150.0D);

			if (targetEntity != null) {
				playerSelected = (Player) targetEntity;
			} else {
				playerSelected = getPlayer();
			}

			if (plugin.isAlly(getPlayer(), playerSelected)) {
				CheckAbilitiesToCast(playerSelected);
			} else {
				CheckAbilitiesToCast(getPlayer());
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

			abilities.add(classBandage);
			abilities.add(classBarrier);
			abilities.add(classHeal);
			abilities.add(classLightHeal);
			SortAbilities();
			abilitiesSet = true;
		}
	}
}