package belven.classes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Bandage;
import belven.classes.Abilities.Barrier;
import belven.classes.Abilities.Heal;
import belven.classes.Abilities.LightHeal;
import belven.timedevents.BarrierTimer;

public class Healer extends Class
{
    public Heal classHeal;
    public LightHeal classLightHeal;
    public Bandage classBandage;
    public Barrier classBarrier;

    public Healer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classHeal = new Heal(this);
        classLightHeal = new LightHeal(this);
        classBandage = new Bandage(this);
        classBarrier = new Barrier(this, 6);
        SetAbilities();
        currentPlayer.setMaxHealth(16);
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;
        CheckAbilitiesToCast(playerSelected);
        className = "Healer";
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            this.CheckAbilitiesToCast(playerSelected);
        }
        else
        {
            playerSelected = classOwner;
            this.CheckAbilitiesToCast(playerSelected);
        }
    }

    @SuppressWarnings("unused")
    public void CheckAbilitiesToCast(Player player)
    {
        if (player.getHealth() <= 10 && classHeal.HasRequirements(classOwner))
        {
            this.classHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
        }
        else if (classBandage.HasRequirements(classOwner))
        {
            this.classBandage.PerformAbility(player);
            classOwner.sendMessage("You gave " + player.getName()
                    + " a bandage");
        }
        else if (!classBarrier.onCooldown
                && classBarrier.HasRequirements(classOwner))
        {
            BukkitTask currentTimer = new BarrierTimer(classBarrier)
                    .runTaskTimer(plugin, 0, 10);

            UltAbilityUsed(classBarrier);

            classOwner.sendMessage("You used " + classBarrier.GetAbilityName());
        }
        else if (classLightHeal.HasRequirements(classOwner))
        {
            this.classLightHeal.PerformAbility(player);
            classOwner.sendMessage("You healed " + player.getName());
        }
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            int currentLevel = classOwner.getLevel();

            if (currentLevel > 1)
            {
                this.AddToAbilities(classBandage);
            }

            if (currentLevel > 3)
            {
                this.AddToAbilities(classHeal);
            }
        }
    }

    public String ListAbilities()
    {
        String ListOfAbilities = "";

        if (Abilities != null)
        {
            for (int i = 0; i < Abilities.size(); i++)
            {
                ListOfAbilities = ListOfAbilities
                        + (Abilities.get(i) != null ? Abilities.get(i)
                                .GetAbilityName() + ", " : "");
            }
            return ListOfAbilities;
        }
        else
            return "";
    }

    public void AddToAbilities(Ability abilityToAdd)
    {
        Abilities.add(abilityToAdd);
    }

    @Override
    public void PerformAbility()
    {

    }

    @Override
    public Player classOwner()
    {
        return classOwner;
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }
}
