package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belvens.classes.resources.functions;

public class LightHeal extends Ability
{
    public LightHeal(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Light Heal";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        Damageable dPlayer = playerToHeal;
        double health = functions.entityCurrentHealthPercent(
                dPlayer.getHealth(), dPlayer.getMaxHealth());

        if (health > 0.5)
        {
            playerToHeal.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION, functions.SecondsToTicks(5),
                    Amplifier(), true));

            currentClass.classOwner.sendMessage("You healed "
                    + playerToHeal.getName());

            return true;
        }
        return false;
    }

    public int Amplifier()
    {
        int PlayerLevel = currentClass.classOwner.getLevel();
        return PlayerLevel > 20 ? 3 : 2;
    }
}
