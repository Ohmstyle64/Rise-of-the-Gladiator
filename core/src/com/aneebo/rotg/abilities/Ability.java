package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ability {
	protected int id, castTime, range;
	protected AbilityType type;
	protected String name;
	protected float timer;
	public boolean available;
	public boolean justCompleted;
	
	public Ability(int id, int castTime, int range, AbilityType type, String name) {
		this.id = id;
		this.castTime = castTime;
		this.range = range;
		this.type = type;
		this.name = name;
		available = false;
		justCompleted = false;
	}
	
	public Ability(Ability ability) {
		this(ability.getId(), ability.getCastTime(), ability.getRange(), ability.getType(), ability.getName());
	}

	public int getId() {
		return id;
	}

	public int getCastTime() {
		return castTime;
	}

	public int getRange() {
		return range;
	}

	public AbilityType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void action(float delta){
		onLoopStart(delta);
		if(timer >=castTime) {
			onLoopEnd();
		}
	}
	public void render(Batch batch){}
	
	protected void onLoopStart(float delta) {
		timer+=delta;
		justCompleted = false;
	}
	
	protected void onLoopEnd() {
		timer = 0;
		available = false;
		justCompleted = true;
	}
	
}
