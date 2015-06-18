package com.aneebo.rotg.base;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class GladiatorObject {
	private String userName;
	private PositionComponent pos;
	private Array<AbilityNameType> abilities;
	private StatComponent stat;
	private RenderComponent render;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public PositionComponent getPos() {
		return pos;
	}

	public void setPos(PositionComponent pos) {
		this.pos = pos;
	}

	public Array<AbilityNameType> getAbilities() {
		return abilities;
	}

	public void setAbilities(Array<AbilityNameType> abilities) {
		this.abilities = abilities;
	}

	public StatComponent getStat() {
		return stat;
	}

	public void setStat(StatComponent stat) {
		this.stat = stat;
	}

	public RenderComponent getRender() {
		return render;
	}

	public void setRender(RenderComponent render) {
		this.render = render;
	}
	
	public Entity loadEntity(Engine engine, FloatingTextManager ftm, boolean withInput) {
		Entity e = new Entity();
		e.add(pos);
		e.add(render);
		e.add(stat);
		if(withInput) 
			e.add(new InputComponent());
		e.add(new CollisionComponent(ColliderType.character));
		Array<Ability> abilityList = new Array<Ability>(abilities.size);
		for(AbilityNameType an : abilities) {
			abilityList.add(Constants.abilityMap.get(an));
		}
		e.add(new AbilityComponent(abilityList,engine,ftm));
		return e;
	}
	
	public String toJsonString() {
		Json json = new Json(OutputType.json);
		System.out.println(json.prettyPrint(this));
		return json.toJson(this, GladiatorObject.class);
	}
	
}
