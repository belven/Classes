package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CripplingArrow implements Ability
{

    @Override
    public void PerformAbility()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {
        // TODO Auto-generated method stub

    }

    public void PerformAbility(Zombie targetZombie)
    {
        targetZombie.addPotionEffect(SlowArrow());
    }
    
    public void PerformAbility(Skeleton targetSkeleton)
    {
        targetSkeleton.addPotionEffect(SlowArrow());
    }

    @Override
    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

    @Override
    public int Amplifier()
    {
        return 3;
    }

    @Override
    public boolean HasRequirements(Player playerToCheck)
    {
        int checksRequired = 0;
        Inventory playerInventory = playerToCheck.getInventory();
        List<ItemStack> requirements = GetAbilityRequirements();

        for (int i = 0; i < requirements.size(); i++)
        {
            if (playerInventory.containsAtLeast(requirements.get(i), 1))
            {
                checksRequired++;
            }
        }

        if (checksRequired == requirements.size())
        {
            ItemStack tempStack;
            int positionID;
            for (int i = 0; i < requirements.size(); i++)
            {
                positionID = playerInventory.first(requirements.get(i)
                        .getType());
                tempStack = playerInventory.getItem(positionID);
                tempStack.setAmount(tempStack.getAmount() - 1);
                // playerInventory.setItem(positionID, tempStack);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public String GetAbilityName()
    {
        return "CripplingArrow";
    }

    @Override
    public List<ItemStack> GetAbilityRequirements()
    {
        List<ItemStack> tempRequirements = new ArrayList<ItemStack>();
        tempRequirements.add(new ItemStack(Material.SNOW_BALL, 1));
        return tempRequirements;
    }

    private PotionEffect SlowArrow()
    {
        return new PotionEffect(PotionEffectType.SLOW, SecondsToTicks(5),
                Amplifier());
    }
}
