package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
import belven.classes.resources.ClassDrop;
import belven.classes.resources.functions;

public class Healer extends Class
{
    public Heal classHeal;
    public LightHeal classLightHeal;
    public Bandage classBandage;
    public Barrier classBarrier;

    public Healer(int Health, Player currentPlayer, ClassManager instance)
    {
        super(Health, currentPlayer, instance);
        className = "Healer";
        SetAbilities();
        SetClassDrops();
    }

    public Healer(Player currentPlayer, ClassManager instance)
    {
        this(8, currentPlayer, instance);
    }

    public void CheckAbilitiesToCast(Player player)
    {
        if (functions.isFood(classOwner.getItemInHand().getType()))
        {
            return;
        }

        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility(player))
                {
                    continue;
                }
                else if (a.shouldBreak)
                {
                    break;
                }
            }
        }
    }

    @Override
    public void SetClassDrops()
    {
        ItemStack lapisBlock = new ItemStack(Material.LAPIS_BLOCK, 6);
        ItemStack woodSword = new ItemStack(Material.WOOD_SWORD);
        ItemStack stick = new ItemStack(Material.STICK);
        ItemStack paper = new ItemStack(Material.PAPER);

        classDrops.add(new ClassDrop(lapisBlock, true));
        classDrops.add(new ClassDrop(woodSword, true));
        classDrops.add(new ClassDrop(paper, 20, 40));
        classDrops.add(new ClassDrop(stick, 20, 40));

        classDrops.add(new ClassDrop(l_Boots(), 60, 100));
        classDrops.add(new ClassDrop(l_ChestPlate(), 60, 100));
        classDrops.add(new ClassDrop(l_Leggings(), 60, 100));
        classDrops.add(new ClassDrop(l_Helmet(), 60, 100));
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {
        Player playerSelected;

        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
        }
        else
        {
            LivingEntity targetEntity = functions.findTargetPlayer(classOwner,
                    150.0D);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
                CheckAbilitiesToCast(playerSelected);
            }
        }

    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        Player playerSelected;

        if (classOwner.isSneaking())
        {
            CheckAbilitiesToCast(classOwner);
        }
        else if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            CheckAbilitiesToCast(playerSelected);
        }
        else
        {
            LivingEntity targetEntity = functions.findTargetPlayer(classOwner,
                    150.0D);

            if (targetEntity != null)
            {
                playerSelected = (Player) targetEntity;
            }
            else
            {
                playerSelected = classOwner;
                CheckAbilitiesToCast(playerSelected);
            }
        }

    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {

    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {

    }

    @Override
    public void SetAbilities()
    {
        classHeal = new Heal(this, 1, 7);
        classLightHeal = new LightHeal(this, 2, 5);
        classBandage = new Bandage(this, 0, 10);
        classBarrier = new Barrier(this, 6, 4, 10);

        Abilities.add(classBandage);
        Abilities.add(classBarrier);
        Abilities.add(classHeal);
        Abilities.add(classLightHeal);
        SortAbilities();

    }
}
