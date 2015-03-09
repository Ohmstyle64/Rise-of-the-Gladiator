package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AbilitySystem extends EntitySystem {
	
	private ImmutableArray<Entity> abilityEntities;
	private ImmutableArray<Entity> posEntities;
	
	private Array<Entity> enemyInRange;
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private PositionComponent ent1Pos;
	private PositionComponent ent2Pos;
	private Entity e;
	private Entity abe;
	
	private Vector2 range;
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		abilityEntities = engine.getEntitiesFor(Family.all(AbilityComponent.class, PositionComponent.class).get());
		posEntities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
		range = new Vector2();
		enemyInRange = new Array<Entity>(4);
	}
	

	@Override
	public void update(float deltaTime) {
		int sizeA = abilityEntities.size();
		int sizeM = posEntities.size();
		for(int i = 0; i < sizeA; i++) {
			abe = abilityEntities.get(i);
			abilityComponent = Mappers.abMap.get(abe);
			abilitySlots = abilityComponent.abilitySlots;
			ent1Pos = Mappers.posMap.get(abe);
			int activatedIndex = -1;
			//CHECK IF ANY ABILITIES CAN BE ACTIVATED
			int slots = abilitySlots.size;
			for(int j = 0; j < slots; j++) {
				//ONLY ONE ABILITY ACTIVE AT A TIME
				if(activatedIndex == -1 && abilitySlots.get(j).isActivated) {
					activatedIndex = j;
				}
				else if(abilitySlots.get(j).isActivating()) {
					abilitySlots.get(activatedIndex).isActivated = false;
					activatedIndex = j;
				}
				else {
					abilitySlots.get(j).isActivated = false;
					continue;
				}

				//CHECK EACH ENTITY
				for(int k = 0; k < sizeM; k++) {
					boolean equal = abe.equals(posEntities.get(k));
					if(equal) continue;
					e = posEntities.get(k);
					ent2Pos = Mappers.posMap.get(e);
					//POSITION OF 2 ENTITIES IS WITHIN RANGE OF ABILITY
					range.set(ent1Pos.curXPos, ent1Pos.curYPos);
					float dst = range.dst(ent2Pos.curXPos, ent2Pos.curYPos);
					if(dst <= abilitySlots.get(j).getRange()) {
						enemyInRange.add(e);
					}else {
						enemyInRange.removeValue(e, true);
					}
				}
			}
			for(int k = 0; k < slots; k++) {
				abilitySlots.get(k).getTargets(abe, (Entity[]) enemyInRange.toArray(Entity.class));
				abilitySlots.get(k).action(deltaTime, abe);
			}
			enemyInRange.clear();
		}
		
	}
	
	public void dispose() {
		
	}
}
