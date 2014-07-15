package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WebArrow extends Ability
{
    public WebArrow(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        requirements.add(new ItemStack(Material.SNOW_BALL, 1));
        currentClass = CurrentClass;
        abilitiyName = "Crippling Arrow";
    }

}
