package belven.timedevents;

import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.Abilities.Ability;

public class AbilityCooldown extends BukkitRunnable
{
    private Ability currentAbility;

    public AbilityCooldown(Ability CurrentAbility)
    {
        currentAbility = CurrentAbility;
    }

    @Override
    public void run()
    {
        currentAbility.currentClass.classOwner.sendMessage(currentAbility
                .GetAbilityName() + " is now availble.");
        currentAbility.onCooldown = false;
        this.cancel();
    }
}
