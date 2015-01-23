package com.aneebo.rotg.utils;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.Fireblast;
import com.aneebo.rotg.abilities.Parry;
import com.aneebo.rotg.abilities.Slash;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class Constants {
	
	public static final boolean DEBUG = false;
	
	//GUI
	public static final int LEFT_BUTTON = 0;
	
	public static final int MAX_ABILITY_SLOTS = 6;
	public static final float TILE_WIDTH = 32f;
	public static final float TILE_HEIGHT = 32f;
	
	//ABILITY
	public static final int AT_SLASH = 0;
	public static final int DF_PARRY = 1;
	public static final int AT_FIREBLAST = 2;
	
	public static final ObjectMap<Integer, Ability> abilityMap = new ObjectMap<Integer, Ability>();
	static {
		//Create abilities
		Slash slash = new Slash(AT_SLASH, 2, 2, AbilityType.offense, "Slash", 5);
		Parry parry = new Parry(DF_PARRY, 2, 3, AbilityType.defense, "Parry", 5);
		Fireblast fireBlast = new Fireblast(AT_FIREBLAST, 2, 5, AbilityType.offense, "Fireblast", 7, new Texture("img/effects/fireball.png"), null);
		
		//Add to map
		abilityMap.put(slash.getId(), slash);
		abilityMap.put(parry.getId(), parry);
		abilityMap.put(fireBlast.getId(), fireBlast);
	}
}
