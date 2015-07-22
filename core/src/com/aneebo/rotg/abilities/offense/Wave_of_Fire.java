package com.aneebo.rotg.abilities.offense;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.abilities.upgrades.Upgrade;
import com.aneebo.rotg.abilities.util.Target;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.ParticleComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.ProjectileComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.AbilityType;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Wave_of_Fire extends Ability {
	
	private PositionComponent pos;
	private StatComponent stat;
	private StatComponent fromStat;
	private PositionComponent projPos;

	public Wave_of_Fire(AbilityNameType nameType, float castTime,
			float range, AbilityType type, String name, float cooldown,
			float damage, float energy_cost, String textureName, Engine engine,
			Array<Upgrade> upgrades) {
		super(nameType, castTime, range, type, name, cooldown, damage, energy_cost,
				textureName, engine, upgrades);
	}

	public Wave_of_Fire(Ability ability, Engine engine) {
		super(ability, engine);
	}

	@Override
	protected void onAbilityStart(Entity me) {
		stat = Mappers.staMap.get(me);
		if(stat.energy - energy_cost < 0) {
			Gdx.app.log(stat.name, "Not enough energy!");
			stat.energy = 0;
			isInterrupted = true;
			return;
		}
		stat.energy -= energy_cost;
		pos = Mappers.posMap.get(me);
		pos.isMoveable = false;
	}

	@Override
	protected void onAbilityEnd(Entity me) {
		Array<Vector2> path = new Array<Vector2>();

		Entity eFireBlast = new Entity();
		pos = Mappers.posMap.get(me);
		stat = Mappers.staMap.get(me);
		projPos = new PositionComponent(pos.gridCurXPos, pos.gridCurYPos, pos.direction);
		
		switch(pos.direction) {
		case Down:
			projPos.gridCurYPos--;
			for(int i = projPos.gridCurYPos; i >= projPos.gridNYPos-this.range; i--) {
				Vector2 cell = new Vector2(projPos.gridCurXPos, i);
				path.add(cell);
			}
			break;
		case Up:
			projPos.gridCurYPos++;
			for(int i = projPos.gridCurYPos; i <= projPos.gridNYPos+this.range; i++) {
				Vector2 cell = new Vector2(projPos.gridCurXPos, i);
				path.add(cell);
			}
			break;
		case Left:
			projPos.gridCurXPos--;
			for(int i = projPos.gridCurXPos; i >= projPos.gridNXPos-this.range; i--) {
				Vector2 cell = new Vector2(i, projPos.gridCurYPos);
				path.add(cell);
			}
			break;
		case Right:
			projPos.gridCurXPos++;
			for(int i = projPos.gridCurXPos; i <= projPos.gridNXPos+this.range; i++) {
				Vector2 cell = new Vector2(i, projPos.gridCurYPos);
				path.add(cell);
			}
			break;
		}
		projPos.gridNXPos = (int) path.get(0).x;
		projPos.gridNYPos = (int) path.get(0).y;
		path.removeIndex(0);
		eFireBlast.add(new RenderComponent(textureName));
		eFireBlast.add(projPos);
		eFireBlast.add(new CollisionComponent(ColliderType.projectile));
		eFireBlast.add(new ProjectileComponent(me, this, path, 3f, damage));
		ParticleEffect pEffect = new ParticleEffect();
		pEffect.load(Gdx.files.internal("img/effects/explosion.p"), Gdx.files.internal("img/effects/"));
		eFireBlast.add(new ParticleComponent(pEffect));
		pEffect.start();
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
	public void hit(ProjectileComponent proj, Entity from, Entity hit) {
		stat = Mappers.staMap.get(hit);
		fromStat = Mappers.staMap.get(from);
		if(stat != null) {
			float damageDealt = (1+fromStat.eValue)*proj.damage;
			stat.health -= damageDealt;
			Gdx.app.log(fromStat.name," has dealt "+damageDealt+" to "+stat.name);
			fromStat.eValue = 0;
		}
		
	}

	@Override
	protected void abilityActing(Entity me) {
		// TODO Auto-generated method stub
		
	}

}
