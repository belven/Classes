package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;

public class LastResort extends Ability
{
    public LastResort(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.NETHER_STAR, 1));
        abilitiyName = "Last Resort";
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        currentPlayer.addPotionEffect(
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, functions
                        .SecondsToTicks(30), 4), false);
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
