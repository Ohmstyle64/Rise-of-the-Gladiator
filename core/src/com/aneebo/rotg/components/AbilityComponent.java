package com.aneebo.rotg.components;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.Parry;
import com.aneebo.rotg.abilities.Slash;
import com.aneebo.rotg.abilities.SuicideBomb;
import com.aneebo.rotg.abilities.range.Fireblast;
import com.aneebo.rotg.abilities.range.RangeAbility;
import com.aneebo.rotg.abilities.range.TeleportMostDst;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class AbilityComponent extends Component {
	public Array<Ability> abilityList;
	public Array<Ability> abilitySlots;
	public ObjectMap<Integer, Ability> abilityMap;
	
	public AbilityComponent(Array<Ability> abilityList, Engine engine) {
		
		this.abilityList = new Array<Ability>(abilityList.size);
		
		abilitySlots = new Array<Ability>();
		
		abilityMap = new ObjectMap<Integer, Ability>();
		
		for(int i = 0; i < abilityList.size; i++) {
			if(abilityList.get(i) instanceof Parry) {
				Parry p = new Parry(abilityList.get(i));
				this.abilityList.add(p);
				abilityMap.put(Constants.DF_PARRY, p);
			}else if(abilityList.get(i) instanceof Slash) {
				Slash s = new Slash(abilityList.get(i));
				this.abilityList.add(s);
				abilityMap.put(Constants.AT_SLASH, s);
			}else if(abilityList.get(i) instanceof Fireblast) {
				Fireblast f = new Fireblast((RangeAbility)abilityList.get(i), engine);
				this.abilityList.add(f);
				abilityMap.put(Constants.AT_FIREBLAST, f);
			}else if(abilityList.get(i) instanceof TeleportMostDst) {
				TeleportMostDst tmd = new TeleportMostDst((RangeAbility)abilityList.get(i), engine);
				this.abilityList.add(tmd);
				abilityMap.put(Constants.DF_TELEPORT_MOST_DST, tmd);
			}else if(abilityList.get(i) instanceof SuicideBomb) {
				SuicideBomb sb = new SuicideBomb(abilityList.get(i));
				this.abilityList.add(sb);
				abilityMap.put(Constants.AT_SUICIDEBOMB, sb);
			}
		}
		//FILL ABILITY SLOTS WITH ABILITY LIST
		abilitySlots.addAll(this.abilityList, 0, Math.min(Constants.MAX_ABILITY_SLOTS,this.abilityList.size));
	}
	
}
