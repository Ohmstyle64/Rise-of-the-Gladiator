package com.aneebo.rotg.utils;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.defense.Blade_Block;
import com.aneebo.rotg.abilities.defense.Force_Field;
import com.aneebo.rotg.abilities.defense.Pure_Heart;
import com.aneebo.rotg.abilities.defense.Teleport;
import com.aneebo.rotg.abilities.offense.Blade_Strike;
import com.aneebo.rotg.abilities.offense.Electric_Charge;
import com.aneebo.rotg.abilities.offense.Explode;
import com.aneebo.rotg.abilities.offense.Ice_Strike;
import com.aneebo.rotg.abilities.offense.Quick_Knife;
import com.aneebo.rotg.abilities.offense.Wave_of_Fire;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Constants {
	//SLOTDATA
	public static final int INVENTORY = 0;
	public static final int EQUIPPED = 1;
	
	public static final boolean DEBUG = false;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	//Levels
	public static final int TEST_LEVEL = 0;
	public static final int CARAVAN_LEVEL = 1;
	
	
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
	
	public static final String WORLD_MAP = "img/map/worldmap.tmx";
	
	public static final String BODY_PLAYER = "img/characters/animations/BODY_male.png";
	public static final String BODY_SKELETON = "img/characters/animations/BODY_skeleton.png";
	
	public static final int MAX_ABILITY_SLOTS = 6;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int MAP_X = 0;
	public static final int MAP_Y = 0;
	public static final int MAP_WIDTH = 800;
	public static final int MAP_HEIGHT = 480;
	public static final int LAST_COL = Constants.MAP_WIDTH / Constants.TILE_WIDTH - 2;
	public static final int LAST_ROW = Constants.MAP_HEIGHT / Constants.TILE_HEIGHT - 2;
	public static final int FIRST_COL = Constants.MAP_X / Constants.TILE_WIDTH + 2;
	public static final int FIRST_ROW = Constants.MAP_Y / Constants.TILE_HEIGHT + 2;
	
	
	//INVENTORY SLOTS
	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int PRIMARY = 3;
	public static final int BLANK = 4;
	
	public static final int INVENTORY_SIZE = 12;
	
	//ABILITY
	public static final int AT_BLADE_STRIKE = 0;
	public static final int AT_WAVE_OF_FIRE = 1;
	public static final int AT_QUICK_KNIFE = 2;
	public static final int AT_ELECTRIC_CHARGE = 3;
	public static final int AT_ICE_STRIKE = 4;
	public static final int AT_EXPLODE = 5;
	public static final int DF_BLADE_BLOCK = 6;
	public static final int DF_PURE_HEART = 7;
	public static final int DF_FORCE_FIELD = 8;
	public static final int DF_TELEPORT = 9;

	public static final ObjectMap<Integer, Ability> abilityMap = new ObjectMap<Integer, Ability>();
	static {
		
		Array<Upgrade> fake = new Array<Upgrade>(3);
		
		//Create abilities
		Blade_Strike bs = new Blade_Strike(AT_BLADE_STRIKE, 2, 2, AbilityType.melee_offensive, "Blade Strike", 5, 10f, 8f, EMPTY_CELL, null, fake);
		Blade_Block bb = new Blade_Block(DF_BLADE_BLOCK, 2, 3, AbilityType.melee_defensive, "Blade Block", 5, 0f, 10f, EMPTY_CELL, null, fake);
		Quick_Knife qk = new Quick_Knife(AT_QUICK_KNIFE, 1, 3, AbilityType.melee_offensive, "Quick Knife", 0, 30f, 0f, EMPTY_CELL, null, fake);
		Force_Field ff = new Force_Field(DF_FORCE_FIELD, 2, 1.5f, AbilityType.magic, "Force Field", 0, 30f, 0f, EMPTY_CELL, null, fake);
		Pure_Heart ph = new Pure_Heart(DF_PURE_HEART, 1, 3, AbilityType.magic, "Pure Heart", 0, 30f, 0f, EMPTY_CELL, null, fake);

		Explode ex = new Explode(AT_EXPLODE, 1, 3, AbilityType.magic, "Explode", 0, 30f, 0f, EMPTY_CELL, null, fake);

		Electric_Charge ec = new Electric_Charge(AT_ELECTRIC_CHARGE, 1, 3, AbilityType.magic, "Pure Heart", 0, 30f, 0f, EMPTY_CELL, null, fake);
		Ice_Strike is = new Ice_Strike(AT_ICE_STRIKE, 1, 3, AbilityType.magic, "Ice Strike", 0, 30f, 0f, EMPTY_CELL, null, fake);
		Wave_of_Fire wof = new Wave_of_Fire(AT_WAVE_OF_FIRE, 2, 8, AbilityType.magic, "Wave of Fire", 2, 20f, 2f, FIREBALL_EFFECT, null, fake);
		Teleport tel = new Teleport(DF_TELEPORT, 2, LAST_COL, AbilityType.magic, "Teleport", 1, 0f, 0f, EMPTY_CELL, null, fake);
		
		//Add to map
		abilityMap.put(bs.getId(), bs);
		abilityMap.put(bb.getId(), bb);
		abilityMap.put(ex.getId(), ex);
		abilityMap.put(wof.getId(), wof);
		abilityMap.put(tel.getId(), tel);
		abilityMap.put(qk.getId(), qk);
		abilityMap.put(ff.getId(), ff);
		abilityMap.put(ph.getId(), ph);
		abilityMap.put(ec.getId(), ec);
		abilityMap.put(is.getId(), is);
	}
}
