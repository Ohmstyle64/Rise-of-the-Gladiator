package com.aneebo.rotg.ai;

import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.types.DirectionType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

public abstract class AiPlan {

	protected Engine engine;
	protected Entity player;
	protected Entity me;
	protected ImmutableArray<Entity> entities;
	private Vector2 dir;
	
	public AiPlan(Entity me, Engine engine) {
		this.engine = engine;
		this.me = me;
		entities = engine.getEntitiesFor(Family.getFor(AIComponent.class, AbilityComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
		dir = new Vector2();
	}
	
	
	public abstract void update(float deltaTime);
	
	protected boolean inAbilityRange(AbilityComponent abil, int abilityId) {
		if(abil.abilityMap.get(abilityId).getTargets(me, new Entity[] {player}).size > 0) {
			return true;
		}
		return false;
	}
	
	protected void correctFacing(PositionComponent mePos, PositionComponent otherPos) {
		//Check if 1 space away
		float d = dir.set(otherPos.curXPos, otherPos.curYPos).sub(mePos.curXPos, mePos.curYPos).len();
		if(d <= 1f) {
			if(dir.x < 0) {
				mePos.direction = DirectionType.Left;
			}else if(dir.x > 0) {
				mePos.direction = DirectionType.Right;
			}else if(dir.y < 0) {
				mePos.direction = DirectionType.Down;
			}else if(dir.y > 0){
				mePos.direction = DirectionType.Up;
			}
		}
	}
}
