package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

public class AbilitySystem extends EntitySystem {
	
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ImmutableArray<Entity> abilityEntities;
	private ImmutableArray<Entity> posEntities;
	
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private PositionComponent ent1Pos;
	private PositionComponent ent2Pos;
	private Entity e;
	
	@Override
	public void addedToEngine(Engine engine) {
		abilityEntities = engine.getEntitiesFor(Family.getFor(AbilityComponent.class, PositionComponent.class));
		posEntities = engine.getEntitiesFor(Family.getFor(PositionComponent.class));
	}
	

	@Override
	public void update(float deltaTime) {
		int sizeA = abilityEntities.size();
		int sizeM = posEntities.size();
		for(int i = 0; i < sizeA; i++) {
			e = abilityEntities.get(i);
			abilityComponent = ac.get(e);
			abilitySlots = abilityComponent.abilitySlots;
			ent1Pos = pc.get(e);
			
			//CHECK IF ANY ABILITIES CAN BE ACTIVATED
			int slots = abilitySlots.size;
			for(int j = 0; j < slots; j++) {
				//CHECK EACH ENTITY
				for(int k = 0; k < sizeM; k++) {
					if(e.equals(posEntities.get(k))) continue;
					e = posEntities.get(k);
					ent2Pos = pc.get(e);
					//POSITION OF 2 ENTITIES IS WITHIN RANGE OF ABILITY
					if(Math.abs(ent2Pos.curXPos-ent1Pos.curXPos) <= abilitySlots.get(j).getRange() &&
							Math.abs(ent2Pos.curYPos-ent1Pos.curYPos) <= abilitySlots.get(j).getRange()) {
						abilitySlots.get(j).available = true;
						//IF THE ABILITY JUST COMPLETED IT'S ACTION THEN STOP
						if(abilitySlots.get(j).justCompleted) {
							abilityComponent.ability = null;
							abilitySlots.get(j).justCompleted = false;
						}
					}else {
						//CHECK IF IT IS AN ACTIVATED ABILITY
						if(abilityComponent.ability != null && abilityComponent.ability == abilitySlots.get(j)) {
							abilityComponent.ability = null;
						}
						abilitySlots.get(j).available = false;
					}
				}
			}
			
			if(abilityComponent.ability==null) continue;
			
			abilityComponent.ability.action(deltaTime);
		}
		
	}
	
	public void dispose() {
		
	}
}
