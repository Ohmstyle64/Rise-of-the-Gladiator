package com.aneebo.rotg.abilities;

import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.utils.Target;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Fireblast extends RangeAbility {
	
	private static int id_Count = 0;
	private PositionComponent pos;
	private StatComponent stat;
	private PositionComponent projPos;

	public Fireblast(int id, int castTime, int range, AbilityType type,
			String name, int cooldown, Texture texture,
			Engine engine) {
		super(id, castTime, range, type, name, cooldown, texture, engine);
		
	}

	public Fireblast(Fireblast fireblast, Engine engine) {
		this(fireblast.id, fireblast.castTime, fireblast.range, fireblast.type,
				fireblast.name, fireblast.cooldown, fireblast.texture, engine);
	}

	@Override
	protected void onAbilityStart(Entity me) {
		pos = Mappers.posMap.get(me);
		pos.isMoveable = false;
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		Array<Vector2> path = new Array<Vector2>();

		Entity eFireBlast = new Entity();
		pos = Mappers.posMap.get(me);
		stat = Mappers.staMap.get(me);
		projPos = new PositionComponent((int)pos.curXPos, (int)pos.curYPos, pos.direction);
		
		switch(pos.direction) {
		case Down:
			projPos.nYPos -= this.range;
			projPos.curYPos--;
			for(int i = (int) projPos.curYPos; i >= projPos.nYPos; i--) {
				Vector2 cell = new Vector2(projPos.curXPos, i);
				path.add(cell);
			}
			break;
		case Up:
			projPos.nYPos += this.range;
			projPos.curYPos++;
			for(int i = (int) projPos.curYPos; i <= projPos.nYPos; i++) {
				Vector2 cell = new Vector2(projPos.curXPos, i);
				path.add(cell);
			}
			break;
		case Left:
			projPos.nXPos -= this.range;
			projPos.curXPos--;
			for(int i = (int) projPos.curXPos; i >= projPos.nXPos; i--) {
				Vector2 cell = new Vector2(i, projPos.curYPos);
				path.add(cell);
			}
			break;
		case Right:
			projPos.nXPos += this.range;
			projPos.curXPos++;
			for(int i = (int) projPos.curXPos; i <= projPos.nXPos; i++) {
				Vector2 cell = new Vector2(i, projPos.curYPos);
				path.add(cell);
			}
			break;
		}
		eFireBlast.add(new RenderComponent(this.texture));
		eFireBlast.add(projPos);
		eFireBlast.add(new CollisionComponent(ColliderType.projectile));
		eFireBlast.add(new StatComponent("fireball"+id_Count++, 0f, 0f, stat.color, 0f, 0f, 3f));
		eFireBlast.add(new ProjectileComponent(me, this, path));
		this.engine.addEntity(eFireBlast);
		pos.isMoveable = true;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		targets.clear();
		targets.addAll(Target.oneDirectional(me, allEnemies, getRange()));
		
		return targets;
	}

}
