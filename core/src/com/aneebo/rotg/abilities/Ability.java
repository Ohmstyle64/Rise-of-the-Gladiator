package com.aneebo.rotg.abilities;

import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
	protected Array<Entity> targets;
	protected Engine engine;
	private Animation cast;
	private Vector2 drawPosition;

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
		Texture t = Assets.assetManager.get(Constants.CASTTIMER, Texture.class);
		TextureRegion r = new TextureRegion(t);
		TextureRegion[][] frames = r.split(32, 32);
		cast = new Animation(castTime / 9, frames[0]);
		drawPosition = new Vector2();
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
			batch.draw(cast.getKeyFrame(castTimeTimer), drawPosition.x, drawPosition.y);
		}
	}

	public void action(float delta, Entity me){
		onLoopStart(delta);
		if(isActivated && justStarted) {
			Gdx.app.log(Mappers.staMap.get(me).name," activates ability "+getName());
			onAbilityStart(me);
			justStarted = false;
		}
		if(isActivated) {
			drawPosition.set(Mappers.posMap.get(me).curXPos*Constants.TILE_WIDTH, (Mappers.posMap.get(me).curYPos + 1)*Constants.TILE_HEIGHT);
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
