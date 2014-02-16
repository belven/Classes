package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MageFireball extends Ability
{
    public MageFireball(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Mage Fireball";
    }

    @Override
    public void PerformAbility(Player mage)
    {
        mage.launchProjectile(Fireball.class);
        return;
    }

    @Override
    public void PerformAbility()
    {

    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner().getLevel() / 7);
    }
}
