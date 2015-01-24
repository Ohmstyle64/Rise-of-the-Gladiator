package com.aneebo.rotg.abilities.util;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Target {
	
	private static final Array<Entity> targets = new Array<Entity>();
	private static PositionComponent pos;
	private static final Vector2 abilityDst = new Vector2();
	
	public static Array<Entity> oneDirectional(Entity me, Entity[] allEnemies, float range) {
		targets.clear();
		
		pos = Mappers.posMap.get(me);
		float mX = pos.curXPos;
		float mY = pos.curYPos;
		
		for(Entity e : allEnemies) {
			pos = Mappers.posMap.get(e);
			abilityDst.set(pos.curXPos, pos.curYPos);
			pos = Mappers.posMap.get(me);
			if(abilityDst.dst(pos.curXPos, pos.curYPos) <= range) {
				switch(pos.direction) {
				case Down:
					pos = Mappers.posMap.get(e);
					if(pos.curYPos < mY) targets.add(e);
					break;
				case Up:
					pos = Mappers.posMap.get(e);
					if(pos.curYPos > mY) targets.add(e);
					break;
				case Left:
					pos = Mappers.posMap.get(e);
					if(pos.curXPos < mX) targets.add(e);
					break;
				case Right:
					pos = Mappers.posMap.get(e);
					if(pos.curXPos > mX) targets.add(e);
					break;
				}
			}
		}
		
		return targets;
	}
}
