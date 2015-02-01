package com.aneebo.rotg.utils;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.Parry;
import com.aneebo.rotg.abilities.Slash;
import com.aneebo.rotg.abilities.range.Fireblast;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class Constants {
	
	public static final boolean DEBUG = false;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	//ASSET PATHS
	public static final String DRAGON_FORM = "img/characters/dragon_form.png";
	public static final String PIG_FORM = "img/characters/pig_form.png";
	public static final String ICE_FORM = "img/characters/ice_form.png";
	public static final String FIREBALL_EFFECT = "img/effects/fireball.png";
	public static final String EMPTY_CELL = "img/gui/empty.png";
	
	public static final int MAX_ABILITY_SLOTS = 6;
	public static final float TILE_WIDTH = 32f;
	public static final float TILE_HEIGHT = 32f;
	
	//INVENTORY SLOTS
	public static final int HEAD = 0;
	public static final int CHEST = 1;
	public static final int LEGS = 2;
	public static final int FEET = 3;
	public static final int HANDS = 4;
	public static final int PRIMARY = 5;
	public static final int SECONDARY = 6;
	
	public static final int INVENTORY_SIZE = 20;
	
	//ABILITY
	public static final int AT_SLASH = 0;
	public static final int DF_PARRY = 1;
	public static final int AT_FIREBLAST = 2;
	
	public static final ObjectMap<Integer, Ability> abilityMap = new ObjectMap<Integer, Ability>();
	static {
		//Create abilities
		Slash slash = new Slash(AT_SLASH, 2, 2, AbilityType.offense, "Slash", 5, 10f, 20f);
		Parry parry = new Parry(DF_PARRY, 2, 3, AbilityType.defense, "Parry", 5, 0f, 10f);
		Fireblast fireBlast = new Fireblast(AT_FIREBLAST, 2, 5, AbilityType.offense, "Fireblast", 7, 20f, 20f, new Texture("img/effects/fireball.png"), null);
		
		//Add to map
		abilityMap.put(slash.getId(), slash);
		abilityMap.put(parry.getId(), parry);
		abilityMap.put(fireBlast.getId(), fireBlast);
	}
}
