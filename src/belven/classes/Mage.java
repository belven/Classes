package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;

import resources.EntityFunctions;
import resources.Functions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.ChainLightning;
import belven.classes.Abilities.LightningStrike;
import belven.classes.Abilities.MageFireball;
import belven.classes.Abilities.Pop;
import belven.classes.resources.ClassDrop;

public class Mage extends Class
{
    public MageFireball classFireball;
    public ChainLightning classChainLightning;
    public LightningStrike classLightningStrike;
    public BlockIterator currentBlockIterator;
    public Pop classPop;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        super(8, currentPlayer, instance);
        className = "Mage";
        SetAbilities();
        SetClassDrops();
    }

    public void CheckAbilitiesToCast()
    {
        LivingEntity targetEntity = EntityFunctions.findTargetEntity(classOwner,
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
        else if (classFireball.HasRequirements(classOwner))
        {
            classFireball.PerformAbility();
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

    @Override
    public void SelfCast(Player currentPlayer)
    {
        CheckAbilitiesToCast();
    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        CheckAbilitiesToCast();
    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager().getType() == EntityType.LIGHTNING)
        {
            event.setDamage(0.0);
        }
        else if (!classLightningStrike.onCooldown)
        {
            Entity entityToStrike = EntityFunctions.GetDamager(event);

            if (entityToStrike != null)
            {
                classLightningStrike.PerformAbility(entityToStrike);
            }
        }
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {

    }

    @Override
    public void SetAbilities()
    {
        classFireball = new MageFireball(this, 1, 5);
        classChainLightning = new ChainLightning(this, 2, 5);
        classLightningStrike = new LightningStrike(this, 3, 5);
        classPop = new Pop(this, 4, 5);

        classLightningStrike.Cooldown = 2;
        Abilities.add(classFireball);
        Abilities.add(classChainLightning);
        Abilities.add(classLightningStrike);
        Abilities.add(classPop);
        SortAbilities();

    }
}
