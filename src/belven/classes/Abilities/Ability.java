package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Ability
{
    List<ItemStack> requirements = new ArrayList<ItemStack>();
    List<Material> inHandRequirements = new ArrayList<Material>();
    public belven.classes.Class currentClass;
    protected String abilitiyName = "";
    public boolean onCooldown = false;
    public int Priority = 0;

    public Ability(int Priority)
    {
        this.Priority = Priority;
    }

    public boolean PerformAbility()
    {
        return false;
    }

    public boolean PerformAbility(Player targetPlayer)
    {
        return false;
    }

    public boolean PerformAbility(Entity targetEntity)
    {
        return false;

    }

    public int Amplifier()
    {
        return 0;
    }

    public boolean HasRequirements(Player playerToCheck)
    {
        int checksRequired = 0;
        Inventory playerInventory = playerToCheck.getInventory();

        if (inHandRequirements.size() > 0)
        {
            if (!inHandRequirements.contains(playerToCheck.getItemInHand()
                    .getType()))
            {
                return false;
            }
            else if (requirements.size() == 0)
            {
                return true;
            }
        }

        for (ItemStack is : requirements)
        {
            if (playerInventory.containsAtLeast(is, is.getAmount()))
            {
                checksRequired++;
            }
        }

        if (checksRequired == requirements.size())
        {
            RemoveItems();
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void RemoveItems()
    {
        ItemStack tempStack;
        Inventory playerInventory = currentClass.classOwner.getInventory();

        int positionID;
        for (ItemStack is : requirements)
        {
            if (is.getType() != Material.NETHER_STAR)
            {
                positionID = playerInventory.first(is.getType());

                tempStack = playerInventory.getItem(positionID);

                if (tempStack.getAmount() > is.getAmount())
                {
                    tempStack.setAmount(tempStack.getAmount() - is.getAmount());
                }
                else
                {
                    tempStack.setType(Material.AIR);
                    playerInventory.setItem(positionID, tempStack);
                }
            }
        }
        currentClass.classOwner.updateInventory();
    }

    public String GetAbilityName()
    {
        return abilitiyName;
    }

    public List<ItemStack> GetAbilityRequirements()
    {
        return requirements;
    }

    public boolean PerformAbility(EntityDamageByEntityEvent event)
    {
        return false;
    }

    public boolean PerformAbility(Location location)
    {
        return false;
    }
}
