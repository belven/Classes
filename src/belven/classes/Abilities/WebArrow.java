package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WebArrow extends Ability
{
    public WebArrow(belven.classes.RPGClass CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        requirements.add(new ItemStack(Material.SNOW_BALL, 1));
        currentClass = CurrentClass;
        abilitiyName = "Webbing Arrow";
    }

}
