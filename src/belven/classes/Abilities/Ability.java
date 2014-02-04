package belven.classes.Abilities;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract interface Ability
{
    public void PerformAbility();

    public void PerformAbility(Player targetPlayer);

    public int SecondsToTicks(int seconds);

    public int Amplifier();

    public boolean HasRequirements(Player playerToCheck);
    
    public String GetAbilityName();
    
    public List<ItemStack> GetAbilityRequirements();
}
