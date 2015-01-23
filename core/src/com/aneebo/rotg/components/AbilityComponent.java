package com.aneebo.rotg.components;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.Fireblast;
import com.aneebo.rotg.abilities.Parry;
import com.aneebo.rotg.abilities.Slash;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Array;

public class AbilityComponent extends Component {
	public Array<Ability> abilityList;
	public Array<Ability> abilitySlots;
	
	public AbilityComponent(Array<Ability> abilityList, Engine engine) {
		
		this.abilityList = new Array<Ability>(abilityList.size);
		this.abilitySlots = new Array<Ability>();
		
		for(int i = 0; i < abilityList.size; i++) {
			if(abilityList.get(i) instanceof Parry) {
				this.abilityList.add(new Parry(abilityList.get(i)));
			}else if(abilityList.get(i) instanceof Slash) {
				this.abilityList.add(new Slash(abilityList.get(i)));
			}else if(abilityList.get(i) instanceof Fireblast) {
				this.abilityList.add(new Fireblast((Fireblast)abilityList.get(i), engine));
			}
		}
		//FILL ABILITY SLOTS WITH ABILITY LIST
		abilitySlots.addAll(this.abilityList, 0, Math.min(Constants.MAX_ABILITY_SLOTS,this.abilityList.size));
	}
	
}
