package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoulDrain extends Ability
{
    public SoulDrain(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        abilitiyName = "Soul Drain";
    }

    @Override
    public boolean PerformAbility(Entity targetEntity)
    {
        if (targetEntity instanceof LivingEntity
                && currentClass.classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {
            ((LivingEntity) targetEntity).addPotionEffect(new PotionEffect(
                    PotionEffectType.HEAL, 1, Amplifier()));
            return true;
        }
        return false;
    }

    @Override
    public int Amplifier()
    {
        return currentClass.classOwner.getLevel() / 4;
    }

}
