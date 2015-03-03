package com.aneebo.rotg.abilities;

import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public abstract class Ability {
	protected int tier;
	protected AbilityType type;
	protected String name;
	protected AbilityNameType nameType;
	protected float castTimeTimer, cooldownTimer, damage, energy_cost, range, castTime, cooldown;
	public boolean isAvailable;
	public boolean isInterrupted;
	public boolean isActivated;
	private boolean justStarted;
	private ProgressBar bar;
	private Label label;
	private Skin skin;
	protected Array<Entity> targets;
	protected Engine engine;

	protected String textureName;
	protected Array<Upgrade> upgrades;
	
	public Ability(AbilityNameType nameType, float castTime, float range, AbilityType type, String name, float cooldown, float damage, float energy_cost, String textureName, Engine engine, Array<Upgrade> upgrades) {
		this.nameType = nameType;
		this.castTime = castTime;
		this.range = range;
		this.type = type;
		this.name = name;
		this.cooldown = cooldown;
		this.damage = damage;
		this.energy_cost = energy_cost;
		this.engine = engine;
		this.textureName = textureName;
		this.upgrades = upgrades;
		justStarted = true;
		isAvailable = false;
		isInterrupted = false;
		isActivated = false;
		castTimeTimer = 0;
		cooldownTimer = cooldown;
		tier = 0;
		targets = new Array<Entity>();
		skin = new Skin(Gdx.files.internal("img/gui/uiskin.json"));
		bar = new ProgressBar(0f,1f,0.01f,false, skin);
		label = new Label(name, skin);
		bar.setPosition(Gdx.graphics.getWidth() / 2 - bar.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f);
		label.setPosition(Gdx.graphics.getWidth() / 2 - label.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f - label.getHeight());
	}
	
	public Ability(Ability ability, Engine engine) {
		this(ability.getNameType(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName(), ability.getCooldown(),
				ability.getDamage(), ability.getEnergy_cost(), ability.getTexture(), engine, ability.getUpgrades());
	}
	


	public AbilityNameType getNameType() {
		return nameType;
	}

	public float getCastTime() {
		return castTime;
	}
	
	public float getCooldown() {
		return cooldown;
	}

	public float getRange() {
		return range;
	}

	public AbilityType getType() {
		return type;
	}
	
	public boolean isActivating() {
		return castTimeTimer > 0;
	}
	
	public float getCastTimeTimer() {
		return castTimeTimer;
	}

	public float getCooldownTimer() {
		return cooldownTimer;
	}

	public String getName() {
		return name;
	}
	
	public String getTexture() {
		return textureName;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public Array<Upgrade> getUpgrades() {
		return upgrades;
	}
	
	public float getDamage() {
		return damage;
	}

	public float getEnergy_cost() {
		return energy_cost;
	}

	public float interrupt() {
		float perc = castTimeTimer / castTime;
		isInterrupted = true;
		if(perc < 0.25f) {
			return 1.5f;
		}else if(perc < 0.5f) {
			return 0.75f;
		}else if(perc < 0.75f) {
			return 0.1f; 
		}else {
			return 0f;
		}
	}
	
	public void render(Batch batch){
		if(isAvailable && isActivated) {
			bar.setVisible(false);
			label.setVisible(false);
		}else {
			bar.setVisible(true);
			label.setVisible(true);
		}
		float perc = castTimeTimer / castTime;
		bar.setValue(perc);
		if(perc < 0.15f) {
			label.getStyle().fontColor = Color.GREEN;
		}else if(perc < 0.25f) {
			label.getStyle().fontColor = Color.CYAN;
		}else if(perc < 0.75f)
			label.getStyle().fontColor = Color.YELLOW;
		else
			label.getStyle().fontColor = Color.RED;
			
		bar.draw(batch, batch.getColor().a);
		label.draw(batch, batch.getColor().a);
	}

	public void action(float delta, Entity me){
		onLoopStart(delta);
		if(isActivated && justStarted) {
			Gdx.app.log(Mappers.staMap.get(me).name," activates ability "+getName());
			onAbilityStart(me);
			justStarted = false;
		}
		if(isActivated) {
			abilityActing(me);
		}
		if(castTimeTimer >=castTime) {
			onAbilityEnd(me);
			cleanUp();
		}
	}
	
	protected abstract void onAbilityStart(Entity me);
	
	protected abstract void onAbilityEnd(Entity me);
	
	protected abstract void abilityActing(Entity me);
	
	public abstract Array<Entity> getTargets(Entity me, Entity[] allEnemies);
	
	public abstract void activateTier1();
	
	public abstract void activateTier2();
	
	public abstract void activateTier3();
	
	public abstract void hit(ProjectileComponent proj, Entity from, Entity hit);

	protected void onLoopStart(float delta) {
		isAvailable = (cooldownTimer >= cooldown);
		if(!isAvailable){
			isActivated = false;
			cooldownTimer+=delta;
			return;
		}
		
		if(!isActivated) {
			return;
		}

		if(isInterrupted) {
			cleanUp();
			return;
		}
		castTimeTimer+=delta;
	}
	
	protected void cleanUp() {
		castTimeTimer = 0;
		cooldownTimer = 0;
		isActivated = false;
		isInterrupted = false;
		justStarted = true;
	}

	public void setTargets(Array<Entity> targets) {
		this.targets = targets;
	}
}
