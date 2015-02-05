package com.aneebo.rotg.level.arena;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AIComponent;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.InventoryComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.items.ChestP2;
import com.aneebo.rotg.inventory.items.PrimP2;
import com.aneebo.rotg.level.Level;
import com.aneebo.rotg.level.Prize;
import com.aneebo.rotg.types.AIState;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.types.Direction;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class TestLevel extends Level {
	
	public TestLevel(Engine engine, Entity player) {
		super(engine, player);
	}

	@Override
	protected void loadLevel() {
		totalRounds = 3;
		mapLocation = "img/arena/arena_1.tmx";
		Array<Item> prizes = new Array<Item>(2);
		prizes.add(new PrimP2());
		prizes.add(new ChestP2());
		prize = new Prize(2, prizes, 100);
		createRounds();
	}

	private void createRounds() {
		//ROUND 1
		Array<Entity> r1entities = new Array<Entity>(1);
		//ENTITIES
		Entity e1 = new Entity();
		e1.add(new PositionComponent(15, 7, Direction.Left));
		e1.add(new RenderComponent(Constants.ICE_FORM));
		e1.add(new CollisionComponent(ColliderType.character));
		e1.add(new AIComponent(AIState.idle));
		Array<Ability> abilities1 = new Array<Ability>();
		abilities1.add(Constants.abilityMap.get(Constants.AT_SLASH));
		e1.add(new AbilityComponent(abilities1, engine));
		e1.add(new StatComponent("Enemy1", 25f, 30f, Color.YELLOW, 1f, 1f, 1.3f));
		r1entities.add(e1);
		
		Round r1 = new Round(r1entities);
		rounds.add(r1);
		
		//ROUND 2
		Array<Entity> r2entities = new Array<Entity>(1);
		//ENTITIES
		Entity e2 = new Entity();
		e2.add(new PositionComponent(15, 7, Direction.Left));
		e2.add(new RenderComponent(Constants.ICE_FORM));
		e2.add(new CollisionComponent(ColliderType.character));
		e2.add(new AIComponent(AIState.idle));
		Array<Ability> abilities2 = new Array<Ability>();
		abilities2.add(Constants.abilityMap.get(Constants.AT_SLASH));
		e2.add(new AbilityComponent(abilities2, engine));
		e2.add(new StatComponent("Enemy2", 35f, 40f, Color.YELLOW, 1f, 1f, 1.3f));
		r2entities.add(e2);
		
		Round r2 = new Round(r2entities);
		rounds.add(r2);
		
		//ROUND 3
		Array<Entity> r3entities = new Array<Entity>(1);
		//ENTITIES
		Entity e3 = new Entity();
		e3.add(new PositionComponent(15, 7, Direction.Left));
		e3.add(new RenderComponent(Constants.ICE_FORM));
		e3.add(new CollisionComponent(ColliderType.character));
		e3.add(new AIComponent(AIState.idle));
		Array<Ability> abilities3 = new Array<Ability>();
		abilities3.add(Constants.abilityMap.get(Constants.AT_SLASH));
		e3.add(new AbilityComponent(abilities3, engine));
		e3.add(new StatComponent("Enemy3", 45f, 50f, Color.YELLOW, 1f, 1f, 1.3f));
		r3entities.add(e3);		
		
		Round r3 = new Round(r3entities);
		rounds.add(r3);
	}

	@Override
	public void entityAdded(Entity entity) {
		
	}

	@Override
	public void entityRemoved(Entity entity) {
		Array<Entity> enemies = rounds.get(currentRound).enemies;
		enemies.removeValue(entity, true);
		if(enemies.size == 0) {
			currentRound++;
			if(isCompleted()) {
				StatComponent stat = Mappers.staMap.get(player);
				InventoryComponent inv = Mappers.invMap.get(player);
				stat.currency+= prize.currency;
				stat.skillPoints+=prize.skillPoints;
				inv.inventory.addItemsToInventory(prize.prize);
				InputComponent input = Mappers.inpMap.get(player);
				input.needRefresh = true;
			}else {
				addEntities();
				StatComponent stat = Mappers.staMap.get(player);
				stat.health = stat.max_health;
				stat.energy = stat.max_energy;
			}
		}
	}
}
