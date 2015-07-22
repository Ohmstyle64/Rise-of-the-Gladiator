package com.aneebo.rotg.utils;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.defense.Blade_Block;
import com.aneebo.rotg.abilities.defense.Force_Field;
import com.aneebo.rotg.abilities.defense.Teleport;
import com.aneebo.rotg.abilities.offense.Blade_Strike;
import com.aneebo.rotg.abilities.offense.DashingStrike;
import com.aneebo.rotg.abilities.offense.Quick_Knife;
import com.aneebo.rotg.abilities.offense.Wave_of_Fire;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.types.AbilityNameType;
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
	
	public static final int WIDTH_LR = 960;
	public static final int HEIGHT_LR = 450;
	public static final int TILEDIM_LR = 64;
	
	public static final int WIDTH_MR = 1280;
	public static final int HEIGHT_MR = 720;
	public static final int TILEDIM_MR = 85;
	
	public static final int WIDTH_HR = 1920;
	public static final int HEIGHT_HR = 1080;
	public static final int TILEDIM_HR = 128;
	
	
	//WarpClient Key
	public static final String API_KEY = "75a8ca517cc741a00d02e8d39f11d5cf577dade0e6043b4636b840150958d43e";
	public static final String SECRET_KEY = "7e3294fc235f275dd233e6f93125e5a98753529ec8298acc9bfa6fd8ed7be96c";
	
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
	public static final String PLAYER_WALK = "img/characters/Walk_Animation.png";
	public static final String PLAYER_SLASH = "img/characters/Slash_Animation.png";
	
	//SOUND EFFECTS
	public static final String TEST_MUSIC = "sounds/music/pilot.mp3";
	
	//MUSIC
	public static final String TEST_GET_HIT = "sounds/sfx/hit_sound.wav";
	
	//PARTICLES
	public static final String PEFFECT_FIRE = "img/effects/explosion.p";
	
	
	public static final String FONT = "img/gui/default.fnt";

	public static final String WORLD_MAP = "img/map/worldmap.tmx";
	public static final String TEST_MAP = "img/arena/arena_1v4.tmx";
	
	//UI
	public static final String UI_SKIN = "img/gui/uiskin.json";
	public static final String UI_ATLAS = "img/gui/uiskin.atlas";
	public static final String CASTTIMER = "img/gui/casttimer.png";
	public static final String HEALTH_ATLAS = "img/gui/healthBlocks.pack";
	
	public static final String BODY_PLAYER = "img/characters/animations/BODY_male.png";
	public static final String BODY_SKELETON = "img/characters/animations/BODY_skeleton.png";
	
	public static final int MAX_ABILITY_SLOTS = 6;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int MAP_X = 224;
	public static final int MAP_Y = 64;
	public static final int MAP_WIDTH = 352;
	public static final int MAP_HEIGHT = 256;
	public static final int LAST_COL = (MAP_X + MAP_WIDTH) / TILE_WIDTH - 1;
	public static final int LAST_ROW = (MAP_Y + MAP_HEIGHT) / TILE_HEIGHT - 1;
	public static final int FIRST_COL = MAP_X / TILE_WIDTH + 1;
	public static final int FIRST_ROW = MAP_Y / TILE_HEIGHT + 1;
	
	
	//INVENTORY SLOTS
	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int PRIMARY = 3;
	public static final int BLANK = 4;
	
	public static final int INVENTORY_SIZE = 12;
	
	public static final ObjectMap<AbilityNameType, Ability> abilityMap = new ObjectMap<AbilityNameType, Ability>();

	static {
		
		Array<Upgrade> fake = new Array<Upgrade>(3);
		
		//Create abilities
		Blade_Strike bs = new Blade_Strike(AbilityNameType.AT_BLADE_STRIKE, 1, 2, AbilityType.melee_offensive, "Blade Strike", 3, 10f, 8f, EMPTY_CELL, null, fake);
		Blade_Block bb = new Blade_Block(AbilityNameType.DF_BLADE_BLOCK, 2, 3, AbilityType.melee_defensive, "Blade Block", 3, 0f, 10f, EMPTY_CELL, null, fake);
		Quick_Knife qk = new Quick_Knife(AbilityNameType.AT_QUICK_KNIFE, 1, 3, AbilityType.melee_offensive, "Quick Knife", 2, 30f, 0f, EMPTY_CELL, null, fake);
		Force_Field ff = new Force_Field(AbilityNameType.DF_FORCE_FIELD, 2, 1.5f, AbilityType.magic, "Force Field", 0, 30f, 0f, EMPTY_CELL, null, fake);

		Wave_of_Fire wof = new Wave_of_Fire(AbilityNameType.AT_WAVE_OF_FIRE, 2, 8, AbilityType.magic, "Wave of Fire", 5, 20f, 2f, FIREBALL_EFFECT, null, fake);
		Teleport tel = new Teleport(AbilityNameType.DF_TELEPORT, 2, LAST_COL, AbilityType.magic, "Teleport", 0, 0f, 0f, EMPTY_CELL, null, fake);
		DashingStrike ds = new DashingStrike(AbilityNameType.AT_DASHING_STRIKE, 0.5f, 3f, AbilityType.magic, "Dashing Strike", 5f, 0f, 0f, EMPTY_CELL, null, fake);
		
		//Add to map
		abilityMap.put(bs.getNameType(), bs);
		abilityMap.put(bb.getNameType(), bb);
		abilityMap.put(wof.getNameType(), wof);
		abilityMap.put(tel.getNameType(), tel);
		abilityMap.put(qk.getNameType(), qk);
		abilityMap.put(ff.getNameType(), ff);
		abilityMap.put(ds.getNameType(), ds);
		
	}
	
	
}
