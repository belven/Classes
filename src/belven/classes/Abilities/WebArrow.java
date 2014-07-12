package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;

public class WebArrow extends Ability
{
    public WebArrow(belven.classes.Class CurrentClass)
    {
        requirements.add(new ItemStack(Material.SNOW_BALL, 1));
        currentClass = CurrentClass;
        abilitiyName = "Crippling Arrow";
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

    public void PerformAbility(Zombie targetZombie)
    {
        targetZombie.addPotionEffect(SlowArrow());
    }

    public void PerformAbility(Skeleton targetSkeleton)
    {
        targetSkeleton.addPotionEffect(SlowArrow());
    }

    @Override
    public int Amplifier()
    {
        return 3;
    }

    private PotionEffect SlowArrow()
    {
        return new PotionEffect(PotionEffectType.SLOW,
                functions.SecondsToTicks(5), Amplifier());
    }
}
