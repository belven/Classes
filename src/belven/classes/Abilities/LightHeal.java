package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;

public class LightHeal extends Ability
{
    public LightHeal(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Light Heal";
        shouldBreak = false;
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        // Damageable dPlayer = playerToHeal;
        // double health = functions.entityCurrentHealthPercent(
        // dPlayer.getHealth(), dPlayer.getMaxHealth());
        //
        // if (health > 0.5)
        // {
        playerToHeal.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, functions.SecondsToTicks(5),
                Amplifier() > 4 ? 4 : Amplifier(), true));

        currentClass.classOwner.sendMessage("You healed "
                + playerToHeal.getName());
        RemoveItems();
        return true;
        // }
        // return false;
    }

    public int Amplifier()
    {
        int PlayerLevel = currentClass.classOwner.getLevel();
        return PlayerLevel / Amplifier;
    }
}
