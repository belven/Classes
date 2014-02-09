package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Pop implements Ability
{
    public belven.classes.Class currentClass;
    private int counter = 0;

    public Pop(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
    }

    public static Entity[] getNearbyEntities(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();

                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
                        + (chZ * 16)).getChunk().getEntities())
                {
                    if (e.getLocation().distance(l) <= radius
                            && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

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

    public void PerformAbility(Location targetLocation)
    {
        Entity[] entitiesToDamage = getNearbyEntities(targetLocation, 4);

        for (int i = 0; i < entitiesToDamage.length; i++)
        {
            if (entitiesToDamage[i] != null
                    && entitiesToDamage[i].getType() != EntityType.PLAYER)
            {
                Vector vectorToUse = currentClass.classOwner().getLocation()
                        .getDirection();
                vectorToUse.setY(vectorToUse.getY() + 1);

                entitiesToDamage[i].setVelocity(vectorToUse);
                
                counter++;
                
                if (counter >= 3)
                {
                    counter = 0;
                    break;
                }
            }
        }
    }

    @Override
    public int SecondsToTicks(int seconds)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int Amplifier()
    {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ItemStack> GetAbilityRequirements()
    {
        List<ItemStack> tempRequirements = new ArrayList<ItemStack>();
        tempRequirements.add(new ItemStack(Material.FEATHER));
        return tempRequirements;
    }
}
