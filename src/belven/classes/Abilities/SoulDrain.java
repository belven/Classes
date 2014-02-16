package belven.classes.Abilities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoulDrain extends Ability
{

    public SoulDrain(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        abilitiyName = "Soul Drain";
    }
    
    @Override
    public void PerformAbility()
    {

    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {

    }

    @Override
    public void PerformAbility(Entity targetEntity)
    {
        if (targetEntity instanceof LivingEntity)
        {
            ((LivingEntity) targetEntity).addPotionEffect(new PotionEffect(
                    PotionEffectType.HEAL, 1, Amplifier()));
        }
    }

    @Override
    public int Amplifier()
    {
        return currentClass.classOwner.getLevel() / 4;
    }

}
