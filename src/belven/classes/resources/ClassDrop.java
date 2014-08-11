package belven.classes.resources;

import org.bukkit.inventory.ItemStack;

import resources.MaterialFunctions;

public class ClassDrop
{
    public ItemStack is;
    public int lowChance;
    public int highChance;
    public int maxAmount = 1;
    public int wildernessAmount = 0;
    public boolean alwaysGive;
    public boolean isArmor;
    public boolean isWilderness = false;

    public ClassDrop(ItemStack item, boolean give, int max)
    {
        is = item;
        alwaysGive = give;
        isArmor = MaterialFunctions.isArmor(is.getType());
        maxAmount = max;
    }

    public ClassDrop(ItemStack item, boolean give, int max,
            int wildernessQuantity)
    {
        this(item, give, max);
        wildernessAmount = wildernessQuantity;
        isWilderness = true;
    }

    public ClassDrop(ItemStack item, int low, int high, int max,
            int wildernessQuantity)
    {
        this(item, low, high, max);
        wildernessAmount = wildernessQuantity;
        isWilderness = true;
    }

    public ClassDrop(ItemStack item, int low, int high, int max)
    {
        is = item;
        lowChance = low;
        highChance = high;
        alwaysGive = false;
        isArmor = MaterialFunctions.isArmor(is.getType());
        maxAmount = max;
    }
}
