package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belvens.classes.resources.functions;

public class Bandage extends Ability
{
    public Bandage(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.PAPER, 1));
        requirements.add(new ItemStack(Material.STICK, 1));
        inHandRequirements.add(Material.PAPER);
        inHandRequirements.add(Material.STICK);
        abilitiyName = "Bandage";
    }

    @Override
    public boolean PerformAbility(Player targetPlayer)
    {
        Damageable dPlayer = targetPlayer;
        double health = functions.entityCurrentHealthPercent(
                dPlayer.getHealth(), dPlayer.getMaxHealth());

        if (health > 0.7)
        {
            targetPlayer.addPotionEffect(new PotionEffect(
                    PotionEffectType.ABSORPTION, functions.SecondsToTicks(20),
                    1), true);
            return true;
        }
        return false;

    }

    @Override
    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }

}
