package com.aneebo.rotg.base;

import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class GladiatorFactory {
	
	private static GladiatorFactory instance;
	private static GladiatorObject go;
	
	private GladiatorFactory() {
		go = new GladiatorObject();
	}
	
	public static GladiatorFactory getInstance() {
		if(instance==null) {
			instance = new GladiatorFactory();
		}
		return instance;
	}
	
	public static GladiatorObject getGOFromJson(String jsonData) {
		Json json = new Json();
		go = json.fromJson(GladiatorObject.class, jsonData);
		return go;
	}
	
	public static GladiatorObject getMaleWarrior() {
		Array<AbilityNameType> abilities = new Array<AbilityNameType>(2);
		abilities.add(AbilityNameType.AT_BLADE_STRIKE);
		abilities.add(AbilityNameType.DF_BLADE_BLOCK);
		
		go.setAbilities(abilities);
		go.setStat(new StatComponent("Default_Male_Warrior", "Male", 35f, 60f, Color.RED, 7, 2, 1.5f, true));
		go.setPos(new PositionComponent(3,4, DirectionType.Right));
		go.setRender(new RenderComponent(Constants.DRAGON_FORM));
		
		return go;
	}
	
	public static void reset() {
		go = new GladiatorObject();
	}
}
