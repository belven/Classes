package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heal extends Ability
{
    public Heal(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Heal";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        Damageable dplayer = (Damageable) playerToHeal;
        if (dplayer.getHealth() <= 10)
        {
            playerToHeal.addPotionEffect(new PotionEffect(
                    PotionEffectType.HEAL, 1, Amplifier()));

            currentClass.classOwner.sendMessage("You healed "
                    + playerToHeal.getName());
            return true;
        }

        return false;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }

}
