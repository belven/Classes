package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;

public class Heal extends Ability
{
    public Heal(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Heal";
        Cooldown = 10;
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

            currentClass.setAbilityOnCoolDown(this, true);
            RemoveItems();
            return true;
        }

        return false;
    }

    public int Amplifier()
    {
        return functions.abilityCap((double) Amplifier,
                (double) currentClass.classOwner.getLevel()) + 1;
    }

}
