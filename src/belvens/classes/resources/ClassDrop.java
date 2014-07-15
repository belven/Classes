package belvens.classes.resources;

import org.bukkit.inventory.ItemStack;

public class ClassDrop
{
    public ItemStack is;
    public int lowChance;
    public int highChance;
    public boolean alwaysGive;
    public boolean isArmor;

    public ClassDrop(ItemStack item, boolean give)
    {
        is = item;
        alwaysGive = give;
        isArmor = functions.isArmor(is.getType());
    }

    public ClassDrop(ItemStack item, int low, int high)
    {
        is = item;
        lowChance = low;
        highChance = high;
        alwaysGive = false;
        isArmor = functions.isArmor(is.getType());
    }
}
