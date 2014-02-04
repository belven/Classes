package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heal implements Ability
{

    public belven.classes.Class currentClass;

    public Heal(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
    }

    @Override
    public void PerformAbility(Player playerToHeal)
    {
        playerToHeal.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,
                SecondsToTicks(1), Amplifier()));
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);

    }

    @Override
    public void PerformAbility()
    {
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner().getLevel() / 7);
    }

    @Override
    public String GetAbilityName()
    {
        return "Heal";
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
                positionID = playerInventory.first(requirements.get(i).getType());
                tempStack = playerInventory.getItem(positionID);
                tempStack.setAmount(tempStack.getAmount() - 1);
                playerInventory.setItem(positionID, tempStack);
            }
            return true;
        }
        else
            return false;
    }

    public List<ItemStack> GetAbilityRequirements()
    {
        List<ItemStack> tempRequirements = new ArrayList<ItemStack>();
        tempRequirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));

        return tempRequirements;
    }
}
