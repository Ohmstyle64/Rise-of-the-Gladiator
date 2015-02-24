package com.aneebo.rotg.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.aneebo.rotg.abilities.Ability;
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
		
		this.abilityList = new Array<Ability>();
		
		abilitySlots = new Array<Ability>();
		
		abilityMap = new ObjectMap<Integer, Ability>();
		
		for(int i = 0; i < abilityList.size; i++) {
			if(abilityList.get(i) instanceof Ability) {
				Class<?> clazz = abilityList.get(i).getClass();
				try {
					Constructor<?> ctor = clazz.getConstructor(Ability.class, Engine.class);
					Ability r = (Ability) ctor.newInstance(abilityList.get(i), engine);
					this.abilityList.add(r);
					abilityMap.put(r.getId(), r);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else if(abilityList.get(i) instanceof Ability) {
				Class<?> clazz = abilityList.get(i).getClass();
				try {
					Constructor<?> ctor = clazz.getConstructor(Ability.class);
					Ability a = (Ability) ctor.newInstance(abilityList.get(i));
					this.abilityList.add(a);
					abilityMap.put(a.getId(), a);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		//FILL ABILITY SLOTS WITH ABILITY LIST
		abilitySlots.addAll(this.abilityList, 0, Math.min(Constants.MAX_ABILITY_SLOTS,this.abilityList.size));
	}
	
}
