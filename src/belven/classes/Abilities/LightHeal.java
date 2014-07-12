package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.functions;

public class LightHeal extends Ability
{
    public LightHeal(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Light Heal";
    }

    @Override
    public void PerformAbility(Player playerToHeal)
    {
        playerToHeal.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, functions.SecondsToTicks(5),
                Amplifier(), true));
    }

    @Override
    public void PerformAbility()
    {
    }

    public int Amplifier()
    {
        int PlayerLevel = currentClass.classOwner.getLevel();
        return PlayerLevel > 20 ? 3 : 2;
    }
}
