package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.MovementComponent;
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
	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ImmutableArray<Entity> abilityEntities;
	private ImmutableArray<Entity> moveEntities;
	
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private MovementComponent ent1Move;
	private MovementComponent ent2Move;
	private Entity e;
	
	@Override
	public void addedToEngine(Engine engine) {
		abilityEntities = engine.getEntitiesFor(Family.getFor(AbilityComponent.class, MovementComponent.class));
		moveEntities = engine.getEntitiesFor(Family.getFor(MovementComponent.class));
	}
	

	@Override
	public void update(float deltaTime) {
		int sizeA = abilityEntities.size();
		int sizeM = moveEntities.size();
		for(int i = 0; i < sizeA; i++) {
			e = abilityEntities.get(i);
			abilityComponent = ac.get(e);
			abilitySlots = abilityComponent.abilitySlots;
			ent1Move = mc.get(e);
			
			//CHECK IF ANY ABILITIES CAN BE ACTIVATED
			int slots = abilitySlots.size;
			for(int j = 0; j < slots; j++) {
				//CHECK EACH ENTITY
				for(int k = 0; k < sizeM; k++) {
					if(e.equals(moveEntities.get(k))) continue;
					e = moveEntities.get(k);
					ent2Move = mc.get(e);
					//POSITION OF 2 ENTITIES IS WITHIN RANGE OF ABILITY
					if(Math.abs(ent2Move.curXPos-ent1Move.curXPos) <= abilitySlots.get(j).getRange() &&
							Math.abs(ent2Move.curYPos-ent1Move.curYPos) <= abilitySlots.get(j).getRange()) {
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
			//ONLY 1 ACTIVE ABILITY AT A TIME
			return;
		}
		
	}
	
	public void dispose() {
		
	}
}
