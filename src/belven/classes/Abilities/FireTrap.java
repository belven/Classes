package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import belven.timedevents.FireTrapTimer;

public class FireTrap implements Ability
{
    public belven.classes.Class currentClass;

    public FireTrap(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
    }

    @Override
    public void PerformAbility()
    {

    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {

    }

    @SuppressWarnings("unused")
    public void PerformAbility(Location targetLocation)
    {
        BukkitTask currentTimer = new FireTrapTimer(targetLocation.getBlock(), 4)
                .runTaskTimer(currentClass.plugin(), SecondsToTicks(5),
                        SecondsToTicks(2));
        targetLocation.getBlock().setType(Material.WOOL);
    }

    @Override
    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }

    @SuppressWarnings("deprecation")
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
                currentClass.classOwner().updateInventory();
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public String GetAbilityName()
    {
        return "FireTrap";
    }

    @Override
    public List<ItemStack> GetAbilityRequirements()
    {
        List<ItemStack> tempRequirements = new ArrayList<ItemStack>();
        tempRequirements.add(new ItemStack(Material.WOOL));
        return tempRequirements;
    }
}
