package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import belven.classes.Abilities.LastResort;
import belven.classes.Abilities.Retaliation;
import belven.classes.resources.ClassDrop;

public class Warrior extends Class
{
    public LastResort currentLastResort;
    public Retaliation currentRetaliation;

    public Warrior(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        currentLastResort = new LastResort(this, 1);
        currentRetaliation = new Retaliation(this, 2);
        className = "Warrior";
        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(40.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
        SortAbilities();
        SetClassDrops();
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {

    }

    @Override
    public void SetClassDrops()
    {
        ItemStack bread = new ItemStack(Material.BREAD);

        ItemStack sword = new ItemStack(Material.STONE_SWORD);

        ItemStack strength = new ItemStack(
                new Potion(PotionType.STRENGTH, 2).toItemStack(1));

        classDrops.add(new ClassDrop(bread, true));
        classDrops.add(new ClassDrop(sword, true));
        classDrops.add(new ClassDrop(strength, 0, 10));
        classDrops.add(new ClassDrop(i_Boots(), true));
        classDrops.add(new ClassDrop(i_ChestPlate(), true));
        classDrops.add(new ClassDrop(i_Leggings(), true));
        classDrops.add(new ClassDrop(i_Helmet(), true));
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

        if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(classOwner))
        {
            UltAbilityUsed(currentLastResort);
            currentLastResort.PerformAbility();
        }
        else if (!currentRetaliation.onCooldown && classOwner.isBlocking())
        {
            currentRetaliation.PerformAbility(event);
            setAbilityOnCoolDown(currentRetaliation, 2);
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageEvent event)
    {
        if (!currentRetaliation.onCooldown && classOwner.isBlocking())
        {
            currentRetaliation.PerformAbility(event);
            setAbilityOnCoolDown(currentRetaliation, 2);
        }
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {

    }
}