package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.inventory.ItemStack;

public class MageFireball extends Ability
{
    public MageFireball(belven.classes.RPGClass CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Mage Fireball";
    }

    @Override
    public boolean PerformAbility()
    {
        currentClass.classOwner.launchProjectile(Fireball.class);
        RemoveItems();
        return true;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }
}
