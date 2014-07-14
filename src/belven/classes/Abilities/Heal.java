package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heal extends Ability
{
    public Heal(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Heal";
    }

    @Override
    public void PerformAbility(Player playerToHeal)
    {
        playerToHeal.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,
                1, Amplifier()));
    }

    @Override
    public void PerformAbility()
    {
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }

}
