package com.aneebo.rotg.abilities;

import java.util.Iterator;

import com.aneebo.rotg.components.InventoryComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public abstract class Ability {
	protected int id, castTime, range, cooldown, tier;
	protected AbilityType type;
	protected String name;
	protected float castTimeTimer, cooldownTimer, damage, energy_cost;
	public boolean isAvailable;
	public boolean isInterrupted;
	public boolean isActivated;
	private boolean justStarted;
	private ProgressBar bar;
	private Label label;
	private Skin skin;
	protected Array<Entity> targets;
	private InventoryComponent invC;
	private ComponentMapper<InventoryComponent> ic = ComponentMapper.getFor(InventoryComponent.class);
	
	protected float increaseToAttackSpeed = 0;
	protected float increaseToDamage = 0;
	protected float increaseToRange = 0;
	protected float damageMitigation = 0;
	protected float magicResist = 0;
	
	public Ability(int id, int castTime, int range, AbilityType type, String name, int cooldown, float damage, float energy_cost) {
		this.id = id;
		this.castTime = castTime;
		this.range = range;
		this.type = type;
		this.name = name;
		this.cooldown = cooldown;
		this.damage = damage;
		this.energy_cost = energy_cost;
		justStarted = true;
		isAvailable = false;
		isInterrupted = false;
		isActivated = false;
		castTimeTimer = 0;
		resetStats();
		cooldownTimer = cooldown;
		tier = 0;
		targets = new Array<Entity>();
		skin = new Skin(Gdx.files.internal("img/gui/uiskin.json"));
		bar = new ProgressBar(0f,1f,0.01f,false, skin);
		label = new Label(name, skin);
		bar.setPosition(Gdx.graphics.getWidth() / 2 - bar.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f);
		label.setPosition(Gdx.graphics.getWidth() / 2 - label.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f - label.getHeight());
	}
	
	private void resetStats() {
		increaseToAttackSpeed = 0;
		increaseToDamage = 0;
		increaseToRange = 0;
		damageMitigation = 0;
		magicResist = 0;
	}
	
	public int getId() {
		return id;
	}

	public int getCastTime() {
		return castTime;
	}
	
	public int getCooldown() {
		return cooldown;
	}

	public int getRange() {
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
	
	
	public float getDamage() {
		return damage;
	}

	public float getEnergy_cost() {
		return energy_cost;
	}

	protected float interrupt() {
		float perc = castTimeTimer / castTime;
		isInterrupted = true;
		if(perc < 0.15f) {
			return 0.5f;
		}else if(perc < 0.25f) {
			return 0.25f;
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
			Gdx.app.log(Mappers.staMap.get(me).name+" activates ability ",getName());
			grabEquipmentStats(me);
			onAbilityStart(me);
			justStarted = false;
		}
		if(castTimeTimer >=castTime) {
			onAbilityEnd(me);
			cleanUp();
		}
	}
	
	private void grabEquipmentStats(Entity me) {
		invC = ic.get(me);
		if(invC == null) return;
		resetStats();
		Iterator<Entry<Integer, Item>> it = invC.inventory.equipped.iterator();
		while(it.hasNext()) {
			Entry<Integer, Item> ent = it.next();
			increaseToAttackSpeed += ent.value.getAttackSpeed();
			castTime *= (1 - MathUtils.clamp(MathUtils.random(increaseToAttackSpeed),0f, 0.5f));
			increaseToDamage += ent.value.getDamage();
			damage += MathUtils.clamp(MathUtils.random(increaseToDamage),0f, 10f);
			increaseToRange += ent.value.getIncreaseToRange();
			range += MathUtils.clamp(MathUtils.random(increaseToRange),0f,10f);
			damageMitigation += ent.value.getDamageMitigation();
			magicResist += ent.value.getMagicResist();
		}
	}
	
	protected abstract void onAbilityStart(Entity me);
	
	protected abstract void onAbilityEnd(Entity me);
	
	public abstract Array<Entity> getTargets(Entity me, Entity[] allEnemies);
	
	public abstract void activateTier1();
	
	public abstract void activateTier2();
	
	public abstract void activateTier3();

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
