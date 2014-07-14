package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;

public class Bandage extends Ability
{
    public Bandage(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.PAPER, 1));
        requirements.add(new ItemStack(Material.STICK, 1));
        inHandRequirements.add(Material.PAPER);
        inHandRequirements.add(Material.STICK);
        abilitiyName = "Bandage";
    }

    @Override
    public void PerformAbility()
    {
    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {
        targetPlayer.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION, functions.SecondsToTicks(20), 1),
                true);
    }

    @Override
    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / 7);
    }

}
