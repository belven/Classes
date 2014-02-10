package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Ability
{
    List<ItemStack> requirements = new ArrayList<ItemStack>();
    public belven.classes.Class currentClass;

    public abstract void PerformAbility();

    public abstract void PerformAbility(Player targetPlayer);

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

    public abstract int Amplifier();

    @SuppressWarnings("deprecation")
    public boolean HasRequirements(Player playerToCheck, int amountToTake)
    {
        int checksRequired = 0;
        Inventory playerInventory = playerToCheck.getInventory();

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

                if (tempStack.getAmount() != 1)
                {
                    tempStack.setAmount(tempStack.getAmount() - amountToTake);
                }
                else
                {
                    tempStack.setType(Material.AIR);
                }

                currentClass.classOwner().updateInventory();
            }
            return true;
        }
        else
            return false;
    }

    public abstract String GetAbilityName();

    public List<ItemStack> GetAbilityRequirements()
    {
        return requirements;
    }
}
