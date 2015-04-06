package belven.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import belven.classes.abilities.Ability;
import belven.resources.ClassDrop;

public abstract class RPGClassData {
	private List<Ability> abilities = new ArrayList<Ability>();
	private boolean abilitiesSet = false;

	protected String baseClassName = "";
	private boolean canCast = true;

	private List<ClassDrop> classDrops = new ArrayList<ClassDrop>();
	protected String className = "";

	private LivingEntity classOwner = null;
	private ClassManager plugin;

	private LivingEntity target;
	public Player targetPlayer;

	public boolean CanCast() {
		return canCast;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}

	public final String getBaseClassName() {
		return baseClassName;
	}

	public List<ClassDrop> getClassDrops() {
		return classDrops;
	}

	public final String getClassName() {
		return className;
	}

	public LivingEntity getOwner() {
		return classOwner;
	}

	public ClassManager getPlugin() {
		return plugin;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public void setCanCast(boolean canCast) {
		this.canCast = canCast;
	}

	public void setClassDrops(List<ClassDrop> classDrops) {
		this.classDrops = classDrops;
	}

	public void setOwner(LivingEntity classOwner) {
		this.classOwner = classOwner;
	}

	public void setPlugin(ClassManager plugin) {
		this.plugin = plugin;
	}

	public void setTarget(LivingEntity target) {
		this.target = target;
	}

	public boolean AbilitiesSet() {
		return abilitiesSet;
	}

	public void setAbilitiesSet(boolean abilitiesSet) {
		this.abilitiesSet = abilitiesSet;
	}

}
