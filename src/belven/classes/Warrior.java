package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import resources.Functions;
import resources.MaterialFunctions;
import belven.classes.Abilities.LastResort;
import belven.classes.Abilities.Retaliation;
import belven.classes.resources.ClassDrop;

public class Warrior extends RPGClass
{
    public LastResort currentLastResort;
    public Retaliation currentRetaliation;

    public Warrior(Player currentPlayer, ClassManager instance)
    {
        super(20, currentPlayer, instance);
        className = "Warrior";

        SetClassDrops();
        SetAbilities();
    }

    @Override
    public void SetClassDrops()
    {
        ItemStack bread = new ItemStack(Material.BREAD);

        ItemStack sword = new ItemStack(Material.STONE_SWORD);

        ItemStack strength = new ItemStack(
                new Potion(PotionType.STRENGTH, 2).toItemStack(1));

        classDrops.add(new ClassDrop(bread, true, 5));
        classDrops.add(new ClassDrop(sword, true, 1, 1));
        classDrops.add(new ClassDrop(strength, 0, 10, 1));
        classDrops.add(new ClassDrop(i_Boots(), true, 1));
        classDrops.add(new ClassDrop(i_ChestPlate(), true, 1));
        classDrops.add(new ClassDrop(i_Leggings(), true, 1));
        classDrops.add(new ClassDrop(i_Helmet(), true, 1));
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {

    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {
        Damageable dcurrentPlayer = classOwner;

        double healthPercent = plugin.GetPlayerE(classOwner).GetHealthPercent();

        this.classOwner.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                Functions.SecondsToTicks(3), (int) (4 * healthPercent)));

        if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(classOwner))
        {
            UltAbilityUsed(currentLastResort);
            currentLastResort.PerformAbility();
        }
        else if (!currentRetaliation.onCooldown && classOwner.isBlocking())
        {
            currentRetaliation.PerformAbility(event);
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageEvent event)
    {
        Damageable dcurrentPlayer = classOwner;

        if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(classOwner))
        {
            UltAbilityUsed(currentLastResort);
            currentLastResort.PerformAbility();
        }
        // else if (!currentRetaliation.onCooldown && classOwner.isBlocking())
        // {
        // currentRetaliation.PerformAbility(event);
        // }
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        if (MaterialFunctions.isAMeeleWeapon(classOwner.getItemInHand()
                .getType()))
        {
            event.setDamage(event.getDamage() + 2);
        }
    }

    @Override
    public void SetAbilities()
    {
        currentLastResort = new LastResort(this, 1, 5);
        currentRetaliation = new Retaliation(this, 2, 2);
        currentRetaliation.Cooldown = 2;
        SortAbilities();
    }
}