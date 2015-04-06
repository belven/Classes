package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.Abilities.Ability;
import belven.classes.player.Abilities.ChainLightning;
import belven.classes.player.Abilities.LightningStrike;
import belven.classes.player.Abilities.MageFireball;
import belven.classes.player.Abilities.Pop;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;

public class Mage extends RPGClass {
	public MageFireball classFireball;
	public ChainLightning classChainLightning;
	public LightningStrike classLightningStrike;
	public BlockIterator currentBlockIterator;
	public Pop classPop;

	public Mage(Player currentPlayer, ClassManager instance) {
		super(8, currentPlayer, instance);
		className = "Mage";
		SetAbilities();
		SetClassDrops();
	}

	public void CheckAbilitiesToCast() {
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
		ItemStack firePot = new Potion(PotionType.FIRE_RESISTANCE, 2).toItemStack(1);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		ItemStack lapisBlock = dye.toItemStack(10);

		ItemStack feather = new ItemStack(Material.FEATHER, 2);

		classDrops.add(new ClassDrop(lapisBlock, true, 20));
		classDrops.add(new ClassDrop(feather, 0, 20, 5));
		classDrops.add(new ClassDrop(firePot, 20, 40, 1));

		classDrops.add(new ClassDrop(l_Boots(), 40, 100, 1));
		classDrops.add(new ClassDrop(l_ChestPlate(), 40, 100, 1));
		classDrops.add(new ClassDrop(l_Leggings(), 40, 100, 1));
		classDrops.add(new ClassDrop(l_Helmet(), 40, 100, 1));
	}

	@Override
	public void SelfCast(Player currentPlayer) {
		CheckAbilitiesToCast();
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
		CheckAbilitiesToCast();
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() == EntityType.LIGHTNING) {
			event.setDamage(0.0);
		} else if (!classLightningStrike.onCooldown) {
			Entity entityToStrike = EntityFunctions.GetDamager(event);
			if (entityToStrike != null) {
				classLightningStrike.PerformAbility();
			}
		}
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {

	}

	@Override
	public void SetAbilities() {
		if (!abilitiesSet) {
			classFireball = new MageFireball(this, 2, 5);
			classChainLightning = new ChainLightning(this, 1, 10);
			classLightningStrike = new LightningStrike(this, 3, 5);
			classPop = new Pop(this, 4, 5);

			classLightningStrike.cooldown = 2;
			abilities.add(classFireball);
			abilities.add(classChainLightning);
			abilities.add(classLightningStrike);
			abilities.add(classPop);
			SortAbilities();
			abilitiesSet = true;
		}
	}
}
