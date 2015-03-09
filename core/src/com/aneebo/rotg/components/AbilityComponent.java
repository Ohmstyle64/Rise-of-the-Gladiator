package com.aneebo.rotg.components;


import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class AbilityComponent extends Component {
	public Array<Ability> abilityList;
	public Array<Ability> abilitySlots;
	public ObjectMap<AbilityNameType, Ability> abilityMap;
	public FloatingTextManager ftm;
	
	public AbilityComponent(Array<Ability> abilityList, Engine engine, FloatingTextManager ftm) {
		this.ftm = ftm;
		this.abilityList = new Array<Ability>();
		
		abilitySlots = new Array<Ability>();
		
		abilityMap = new ObjectMap<AbilityNameType, Ability>();
		
		for(int i = 0; i < abilityList.size; i++) {
			Class<?> clazz = abilityList.get(i).getClass();
			try {
				Constructor ctor = ClassReflection.getConstructor(clazz, Ability.class, Engine.class);
				Ability a = (Ability) ctor.newInstance(abilityList.get(i), engine);
				this.abilityList.add(a);
				abilityMap.put(a.getNameType(), a);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
		}
		
		//FILL ABILITY SLOTS WITH ABILITY LIST
		abilitySlots.addAll(this.abilityList, 0, Math.min(Constants.MAX_ABILITY_SLOTS,this.abilityList.size));
	}
	
}
