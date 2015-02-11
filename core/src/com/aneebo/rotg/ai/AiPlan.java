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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class AiPlan {

	protected Engine engine;
	protected Entity player;
	protected Entity me;
	protected ImmutableArray<Entity> entities;
	private Vector2 dir;
	private Vector2 ref;
	
	public AiPlan(Entity me, Engine engine) {
		this.engine = engine;
		this.me = me;
		entities = engine.getEntitiesFor(Family.getFor(AIComponent.class, AbilityComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
		dir = new Vector2();
		ref = new Vector2();
	}
	
	
	public abstract void update(float deltaTime);
	
	protected boolean inAbilityRange(AbilityComponent abil, int abilityId) {
		if(abil.abilityMap.get(abilityId).getTargets(me, new Entity[] {player}).size > 0) {
			return true;
		}
		return false;
	}
	
	protected void correctFacing(PositionComponent mePos, PositionComponent otherPos) {
		float angle = dir.set(mePos.curXPos, mePos.curYPos).angle(ref.set(otherPos.curXPos, otherPos.curYPos));
		if(angle < 0) angle += 360;
		angle *= MathUtils.degRad;
		
		if(angle > MathUtils.PI*(1/4) && angle < MathUtils.PI*(3/4)) {
			mePos.direction = DirectionType.Up;
		}else if(angle >= MathUtils.PI*(3/4) && angle <= MathUtils.PI*(5/4)) {
			mePos.direction = DirectionType.Left;
		}else if(angle > MathUtils.PI*(5/4) && angle < MathUtils.PI*(7/4)) {
			mePos.direction = DirectionType.Down;
		}else 
			mePos.direction = DirectionType.Right;
	}
}
