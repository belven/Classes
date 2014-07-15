package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.ChainLightning;
import belven.classes.Abilities.LightningStrike;
import belven.classes.Abilities.MageFireball;
import belven.classes.Abilities.Pop;
import belvens.classes.resources.ClassDrop;
import belvens.classes.resources.functions;

public class Mage extends Class
{
    public MageFireball classFireball;
    public ChainLightning classChainLightning;
    public LightningStrike classLightningStrike;
    public BlockIterator currentBlockIterator;
    public Pop classPop;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classFireball = new MageFireball(this, 1);
        classChainLightning = new ChainLightning(this, 2);
        classLightningStrike = new LightningStrike(this, 3);
        classPop = new Pop(this, 4);
        className = "Mage";
        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(16.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
        SortAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;

        CheckAbilitiesToCast(playerSelected, currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            CheckAbilitiesToCast(playerSelected, classOwner);
        }
        else
        {
            playerSelected = classOwner;
            CheckAbilitiesToCast(playerSelected, classOwner);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        LivingEntity targetEntity = functions.findTargetEntity(classOwner,
                150.0D);

        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility(targetEntity.getLocation()))
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
        }

        if (!classChainLightning.onCooldown
                && classChainLightning.HasRequirements(classOwner))
        {
            classChainLightning.PerformAbility();

            UltAbilityUsed(classChainLightning);
        }
        else if (classPop.HasRequirements(classOwner))
        {
            if (targetEntity != null)
            {
                classPop.PerformAbility(targetEntity.getLocation());
            }
        }
        else if (classFireball.HasRequirements(player))
        {
            classFireball.PerformAbility(player);
        }
    }

    @SuppressWarnings("deprecation")
    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (event.getDamager().getType() == EntityType.LIGHTNING)
        {
            event.setDamage(0.0);
        }
        else if (!classLightningStrike.onCooldown)
        {
            Entity entityToStrike = event.getDamager();

            if (entityToStrike.getType() == EntityType.ARROW)
            {
                Arrow entityArrow = (Arrow) entityToStrike;
                entityToStrike = entityArrow.getShooter();
            }

            classLightningStrike.PerformAbility(entityToStrike);
            setAbilityOnCoolDown(classLightningStrike, 2);
        }

    }

    @Override
    public void SetClassDrops()
    {
        ItemStack firePot = new Potion(PotionType.FIRE_RESISTANCE, 2)
                .toItemStack(1);

        ItemStack lapisBlock = new ItemStack(Material.LAPIS_BLOCK, 10);
        ItemStack feather = new ItemStack(Material.FEATHER, 2);

        classDrops.add(new ClassDrop(lapisBlock, true));
        classDrops.add(new ClassDrop(feather, 0, 20));
        classDrops.add(new ClassDrop(firePot, 20, 40));

        classDrops.add(new ClassDrop(l_Boots(), 40, 100));
        classDrops.add(new ClassDrop(l_ChestPlate(), 40, 100));
        classDrops.add(new ClassDrop(l_Leggings(), 40, 100));
        classDrops.add(new ClassDrop(l_Helmet(), 40, 100));
    }
}
