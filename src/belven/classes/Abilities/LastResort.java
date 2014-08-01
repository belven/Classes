package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.Functions;

public class LastResort extends Ability
{
    public LastResort(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.NETHER_STAR, 1));
        abilitiyName = "Last Resort";
    }

    @Override
    public boolean PerformAbility()
    {
        currentClass.classOwner.addPotionEffect(
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions
                        .SecondsToTicks(Amplifier()), 4), false);
        return true;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / Amplifier);
    }
}
