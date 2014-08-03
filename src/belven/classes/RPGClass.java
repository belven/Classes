package belven.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import resources.Functions;
import belven.classes.Abilities.Ability;
import belven.classes.resources.ClassDrop;
import belven.classes.timedevents.AbilityCooldown;

public abstract class RPGClass
{
    public ArrayList<Ability> Abilities = new ArrayList<Ability>();
    public Player classOwner = null;
    public ClassManager plugin;
    protected String className = "";
    protected String baseClassName = "";
    public List<ClassDrop> classDrops = new ArrayList<ClassDrop>();
    public boolean CanCast = true;

    public RPGClass(double health, Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;

        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(health * 2);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
    }

    public final String getClassName()
    {
        return className;
    }

    public void SetClassDrops()
    {

    }

    public void RemoveClassDrop(Material m)
    {
        Iterator<ClassDrop> tempDrops = classDrops.iterator();

        while (tempDrops.hasNext())
        {
            ClassDrop cd = tempDrops.next();
            if (cd.is.getType() == m)
            {
                tempDrops.remove();
            }
        }
    }

    public void ListAbilities()
    {
        for (Ability a : Abilities)
        {
            classOwner.sendMessage(a.GetAbilityName());
        }
    }

    public void SortAbilities()
    {
        SortAbilities(Abilities);
    }

    public void SortAbilities(List<Ability> tempAbilities)
    {
        Collections.sort(tempAbilities, new Comparator<Ability>()
        {
            @Override
            public int compare(Ability a1, Ability a2)
            {
                if (a1.Priority > a2.Priority)
                {
                    return 1;
                }
                else if (a1.Priority < a2.Priority)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }

    public final String getBaseClassName()
    {
        return baseClassName;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (other == this)
        {
            return true;
        }
        else if (!(other instanceof RPGClass))
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    public abstract void SelfCast(Player currentPlayer);

    public abstract void SetAbilities();

    public abstract void RightClickEntity(Entity currentEntity);

    public abstract void SelfTakenDamage(EntityDamageByEntityEvent event);

    public abstract void SelfDamageOther(EntityDamageByEntityEvent event);

    public void ToggleSneakEvent(PlayerToggleSneakEvent event)
    {
    }

    public void UltAbilityUsed(Ability currentAbility)
    {
        setAbilityOnCoolDown(currentAbility);
    }

    public void setAbilityOnCoolDown(Ability currentAbility, boolean sendMessage)
    {
        new AbilityCooldown(currentAbility, sendMessage).runTaskLater(plugin,
                Functions.SecondsToTicks(currentAbility.Cooldown));
        currentAbility.onCooldown = true;
    }

    public void setAbilityOnCoolDown(Ability currentAbility)
    {
        setAbilityOnCoolDown(currentAbility, false);
    }

    public ItemStack l_ChestPlate()
    {
        return new ItemStack(Material.LEATHER_CHESTPLATE);
    }

    public ItemStack l_Leggings()
    {
        return new ItemStack(Material.LEATHER_LEGGINGS);
    }

    public ItemStack l_Helmet()
    {
        return new ItemStack(Material.LEATHER_HELMET);
    }

    public ItemStack l_Boots()
    {
        return new ItemStack(Material.LEATHER_BOOTS);
    }

    public ItemStack i_ChestPlate()
    {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    public ItemStack i_Leggings()
    {
        return new ItemStack(Material.IRON_LEGGINGS);
    }

    public ItemStack i_Helmet()
    {
        return new ItemStack(Material.IRON_HELMET);
    }

    public ItemStack i_Boots()
    {
        return new ItemStack(Material.IRON_BOOTS);
    }

    public void SelfTakenDamage(EntityDamageEvent event)
    {
        // TODO Auto-generated method stub

    }

    public void AbilityUsed(Ability ability)
    {
        // TODO Auto-generated method stub

    }

}
