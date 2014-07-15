package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belvens.classes.resources.functions;

public class SetAlight extends Ability
{
    public SetAlight(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.FIREWORK_CHARGE, 1));
        abilitiyName = "Set Alight";
    }

    @Override
    public boolean PerformAbility(Player playerToBurn)
    {
        int Amplifier = Amplifier();

        if (Amplifier > 10)
        {
            Amplifier = 10;
        }

        playerToBurn.setFireTicks(functions.SecondsToTicks(Amplifier));
        return true;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 5) + 2;
    }

}