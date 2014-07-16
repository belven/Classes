package belven.classes.Abilities;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import belven.classes.resources.functions;

public class Cleanse extends Ability
{
    public Cleanse(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.GLOWSTONE_DUST, 1));
        abilitiyName = "Cleanse";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        playerToHeal.setFireTicks(0);

        Iterator<PotionEffect> ActivePotionEffects = playerToHeal
                .getActivePotionEffects().iterator();

        while (ActivePotionEffects.hasNext())
        {
            PotionEffect pe = ActivePotionEffects.next();
            if (functions.debuffs(pe.getType()))
            {
                ActivePotionEffects.remove();
                break;
            }
        }
        return true;

    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }

}
