package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.Abilities.HealingFurry;

public class Monk extends Healer
{
    public HealingFurry classHealingFurry;

    public Monk(Player currentPlayer, ClassManager instance)
    {
        super(12, currentPlayer, instance);
        className = "Monk";
        baseClassName = "Healer";
        SetClassDrops();
        SetAbilities();
    }

    @Override
    public void SetAbilities()
    {
        super.SetAbilities();

        Abilities.remove(classHeal);

        classHealingFurry = new HealingFurry(this, 0, 5);

        classHealingFurry.Cooldown = 8;
        classLightHeal.Priority = 10;
        classBandage.Amplifier = 5;
        classLightHeal.Amplifier = 2;

        Abilities.add(classHealingFurry);
        SortAbilities();
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        RemoveClassDrop(Material.WOOD_SWORD);
    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        if (classOwner.getItemInHand() == null
                || classOwner.getItemInHand().getType() != Material.AIR)
        {
            event.setDamage(event.getDamage() + 6.0);
        }
    }
}
