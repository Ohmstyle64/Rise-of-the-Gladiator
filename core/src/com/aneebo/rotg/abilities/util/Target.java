package com.aneebo.rotg.abilities.util;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Target {
	
	private static final Array<Entity> targets = new Array<Entity>();
	private static PositionComponent pos;
	private static ProjectileComponent proj;
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
	
	public static Array<Entity> firstInRow(Entity me, Entity[] allEntities, float range) {
		targets.clear();
		pos = Mappers.posMap.get(me);
		float diff = 0;
		for(Entity e : allEntities) {
			switch(pos.direction) {
			case Down :
				diff = pos.curYPos - Mappers.posMap.get(e).curYPos;
				if(Mappers.posMap.get(e).curXPos == pos.curXPos &&
						diff <= range && diff > 0) {
					if(targets.size == 0) {
						targets.add(e);
					} else if(Mappers.posMap.get(targets.first()).curYPos < Mappers.posMap.get(e).curYPos) {
						targets.removeIndex(0);
						targets.add(e);
					}
				}
				break;
			case Up :
				diff = Mappers.posMap.get(e).curYPos -  pos.curYPos;
				if(Mappers.posMap.get(e).curXPos == pos.curXPos &&
						diff <= range && diff > 0) {
					if(targets.size == 0) {
						targets.add(e);
					} else if(Mappers.posMap.get(targets.first()).curYPos > Mappers.posMap.get(e).curYPos) {
						targets.removeIndex(0);
						targets.add(e);
					}
				}
				break;
			case Left :
				diff = pos.curXPos - Mappers.posMap.get(e).curXPos;
				if(Mappers.posMap.get(e).curYPos == pos.curYPos &&
						diff <= range && diff > 0) {
					if(targets.size == 0) {
						targets.add(e);
					} else if(Mappers.posMap.get(targets.first()).curYPos < Mappers.posMap.get(e).curYPos) {
						targets.removeIndex(0);
						targets.add(e);
					}
				}
				break;
			case Right :
				diff = Mappers.posMap.get(e).curXPos -  pos.curXPos;
				if(Mappers.posMap.get(e).curYPos == pos.curYPos &&
						diff <= range && diff > 0) {
					if(targets.size == 0) {
						targets.add(e);
					} else if(Mappers.posMap.get(targets.first()).curXPos > Mappers.posMap.get(e).curXPos) {
						targets.removeIndex(0);
						targets.add(e);
					}
				}
				break;
			}
			
		}
		
		return targets;
	}
	
	public static Array<Entity> incommingProjectiles(Entity me, Entity[] allEntities, float range) {
		targets.clear();
		
		for(Entity e : allEntities) {
			proj = Mappers.projMap.get(e);
			if(proj== null) continue;
			targets.add(e);
		}
		return targets;
	}
}
