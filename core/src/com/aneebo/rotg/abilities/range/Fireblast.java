package com.aneebo.rotg.abilities.range;

import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.types.ColliderType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Fireblast extends RangeAbility {
	
	private static int id_Count = 0;
	private static final float DAMAGE = 25f;
	private static final float ENERGY_COST = 25f;
	
	private PositionComponent pos;
	private StatComponent stat;
	private StatComponent fromStat;
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
		stat = Mappers.staMap.get(me);
		if(stat.energy - ENERGY_COST < 0) {
			Gdx.app.log(stat.name, "Not enough energy!");
			stat.energy = 0;
			isInterrupted = true;
			return;
		}
		stat.energy -= ENERGY_COST;
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
			projPos.curYPos--;
			for(int i = (int) projPos.curYPos; i >= projPos.nYPos-this.range; i--) {
				Vector2 cell = new Vector2(projPos.curXPos, i);
				path.add(cell);
			}
			break;
		case Up:
			projPos.curYPos++;
			for(int i = (int) projPos.curYPos; i <= projPos.nYPos+this.range; i++) {
				Vector2 cell = new Vector2(projPos.curXPos, i);
				path.add(cell);
			}
			break;
		case Left:
			projPos.curXPos--;
			for(int i = (int) projPos.curXPos; i >= projPos.nXPos-this.range; i--) {
				Vector2 cell = new Vector2(i, projPos.curYPos);
				path.add(cell);
			}
			break;
		case Right:
			projPos.curXPos++;
			for(int i = (int) projPos.curXPos; i <= projPos.nXPos+this.range; i++) {
				Vector2 cell = new Vector2(i, projPos.curYPos);
				path.add(cell);
			}
			break;
		}
		projPos.nXPos = (int) path.get(0).x;
		projPos.nYPos = (int) path.get(0).y;
		path.removeIndex(0);
		eFireBlast.add(new RenderComponent(this.texture));
		eFireBlast.add(projPos);
		eFireBlast.add(new CollisionComponent(ColliderType.projectile));
		eFireBlast.add(new ProjectileComponent(me, this, path, 3f));
		this.engine.addEntity(eFireBlast);
		pos.isMoveable = true;
	}

	@Override
	public Array<Entity> getTargets(Entity me, Entity[] allEnemies) {
		targets.clear();
		targets.addAll(Target.oneDirectional(me, allEnemies, getRange()));
		
		return targets;
	}

	@Override
	public void hit(Entity from, Entity hit) {
		stat = Mappers.staMap.get(hit);
		fromStat = Mappers.staMap.get(from);
		if(stat != null) {
			stat.health -= (1-(stat.eValue + fromStat.eValue))*DAMAGE;
			fromStat.eValue = 0;
		}
		
	}

}
