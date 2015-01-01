package com.aneebo.rotg.components;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class AbilityComponent extends Component {
	public Ability ability;
	public Array<Ability> abilityList;
	public Array<Ability> abilitySlots;
	
	public AbilityComponent(Array<Ability> abilityList) {
		
		this.abilityList = new Array<Ability>(abilityList.size);
		this.abilitySlots = new Array<Ability>();
		
		for(int i = 0; i < abilityList.size; i++) {
			this.abilityList.add(new Ability(abilityList.get(i)));
		}
		//FILL ABILITY SLOTS WITH ABILITY LIST
		abilitySlots.addAll(abilityList, 0, Math.min(Constants.MAX_ABILITY_SLOTS,abilityList.size));
	}
}
