package com.aneebo.rotg.utils;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.Parry;
import com.aneebo.rotg.abilities.Slash;
import com.aneebo.rotg.abilities.range.Fireblast;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public class Constants {
	//SLOTDATA
	public static final int INVENTORY = 0;
	public static final int EQUIPPED = 1;
	
	public static final boolean DEBUG = false;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	//ASSET PATHS
	public static final String DRAGON_FORM = "img/characters/dragon_form.png";
	public static final String PIG_FORM = "img/characters/pig_form.png";
	public static final String ICE_FORM = "img/characters/ice_form.png";
	public static final String FIREBALL_EFFECT = "img/effects/fireball.png";
	public static final String EMPTY_CELL = "img/gui/empty.png";
	public static final String HELMET_1 = "img/item/armor/helmet2_etched.png";
	public static final String PLATE_MAIL_1 = "img/item/armor/plate_mail1.png";
	public static final String ELVEN_SCALEMAIL_1 = "img/item/armor/elven_scalemail.png";
	public static final String SHORTSWORD_1 = "img/item/weapon/short_sword2.png";
	public static final String SPEAR_1 = "img/item/weapon/spear2.png";
	
	public static final int MAX_ABILITY_SLOTS = 6;
	public static final float TILE_WIDTH = 32f;
	public static final float TILE_HEIGHT = 32f;
	
	//INVENTORY SLOTS
	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int PRIMARY = 3;
	public static final int BLANK = 4;
	
	public static final int INVENTORY_SIZE = 12;
	
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
