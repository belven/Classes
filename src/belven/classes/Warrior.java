package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import belven.classes.Abilities.LastResort;
import belven.classes.Abilities.Retaliation;
import belvens.classes.resources.ClassDrop;

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
        Damageable dcurrentPlayer = (Damageable) damagedPlayer;
        if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5
                && currentLastResort.HasRequirements(damagedPlayer))
        {
            UltAbilityUsed(currentLastResort);
            currentLastResort.PerformAbility(damagedPlayer);
        }
        else if (!currentRetaliation.onCooldown && damagedPlayer.isBlocking())
        {
            currentRetaliation.PerformAbility(event);
            setAbilityOnCoolDown(currentRetaliation, 2);
        }
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
}