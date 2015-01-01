package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Ability {
	protected int id, castTime, range;
	protected AbilityType type;
	protected String name;
	protected float timer;
	public boolean isAvailable;
	public boolean isCompleted;
	public boolean isInterrupted;
	private ProgressBar bar;
	private Label label;
	
	public Ability(int id, int castTime, int range, AbilityType type, String name) {
		this.id = id;
		this.castTime = castTime;
		this.range = range;
		this.type = type;
		this.name = name;
		isAvailable = false;
		isCompleted = false;
		isInterrupted = false;
		Skin skin = new Skin(Gdx.files.internal("img/gui/uiskin.json"));
		bar = new ProgressBar(0f,1f,0.01f,false, skin);
		label = new Label(name, skin);
		
		bar.setPosition(Gdx.graphics.getWidth() / 2 - bar.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f);
		label.setPosition(Gdx.graphics.getWidth() / 2 - label.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f - label.getHeight());
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
		if(isInterrupted) {
			timer = 0;
			isInterrupted = false;
		}
		onLoopStart(delta);
		if(timer >=castTime) {
			onLoopEnd();
		}
	}
	
	public void render(Batch batch){
		float perc = timer / castTime;
		bar.setValue(perc);
		if(perc < 0.1f) {
			label.getStyle().fontColor = Color.GREEN;
		}else if(perc < 0.6f) {
			label.getStyle().fontColor = Color.YELLOW;
		}else
			label.getStyle().fontColor = Color.RED;
		
		bar.draw(batch, batch.getColor().a);
		label.draw(batch, batch.getColor().a);
	}
	
	protected void onLoopStart(float delta) {
		timer+=delta;
		isCompleted = false;
	}
	
	protected void onLoopEnd() {
		timer = 0;
		isAvailable = false;
		isCompleted = true;
	}
	
}
