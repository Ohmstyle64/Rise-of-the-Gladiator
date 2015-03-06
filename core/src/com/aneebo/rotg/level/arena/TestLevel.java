package com.aneebo.rotg.level.arena;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.ai.BasicMeleeAI;
import com.aneebo.rotg.ai.BasicRangeAI;
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
import com.aneebo.rotg.screens.Play;
import com.aneebo.rotg.types.AbilityNameType;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.types.LevelType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TestLevel extends Level implements EntityListener {
	
	public TestLevel(Engine engine, Entity player, Vector2 playerStart) {
		super(engine, player, "img/arena/arena_1.tmx", playerStart);
	}

	@Override
	protected void loadLevel() {
		engine.addEntityListener(this);
		currentRound = 0;
		totalRounds = 3;
		Array<Item> prizes = new Array<Item>(2);
		prizes.add(new PrimP2());
		prizes.add(new ChestP2());
		prize = new Prize(2, prizes, 100);
		rounds.clear();
		createRounds();
		
		PositionComponent pos = Mappers.posMap.get(player);
		pos.curXPos = playerStart.x;
		pos.curYPos = playerStart.y;
		pos.gridNXPos = (int) playerStart.x;
		pos.gridNYPos = (int) playerStart.y;
		pos.gridCurXPos = (int) playerStart.x;
		pos.gridCurYPos = (int) playerStart.y;
	}

	private void createRounds() {
		//ROUND 1
		Array<Entity> r1entities = new Array<Entity>(2);
		//ENTITIES
//		Entity r1e1 = new Entity();
//		r1e1.add(new PositionComponent(11, 5, DirectionType.Left));
//		r1e1.add(new RenderComponent(Constants.ICE_FORM));
//		r1e1.add(new CollisionComponent(ColliderType.character));
//		r1e1.add(new AIComponent(new BasicRangeAI(r1e1, engine)));
//		Array<Ability> abilities_r1e1 = new Array<Ability>();
//		abilities_r1e1.add(Constants.abilityMap.get(AbilityNameType.AT_WAVE_OF_FIRE));
//		abilities_r1e1.add(Constants.abilityMap.get(AbilityNameType.DF_TELEPORT));
//		r1e1.add(new AbilityComponent(abilities_r1e1, engine));
//		r1e1.add(new StatComponent("Range_Enemy_1", 25f, 60f, Color.YELLOW, 2f, 2f, 1.3f));
//		r1entities.add(r1e1);
		
		Entity r1e1 = new Entity();
		r1e1.add(new PositionComponent(15, 5, DirectionType.Left));
		r1e1.add(new RenderComponent(Constants.PIG_FORM));
		r1e1.add(new CollisionComponent(ColliderType.character));
		r1e1.add(new AIComponent(new BasicMeleeAI(r1e1, engine)));
		Array<Ability> abilities_r1e2 = new Array<Ability>();
		abilities_r1e2.add(Constants.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE));
		r1e1.add(new AbilityComponent(abilities_r1e2, engine));
		r1e1.add(new StatComponent("Close_Enemy_1", 25f, 60f, Color.YELLOW, 2f, 2f, 1.5f));
		r1entities.add(r1e1);
		
		Entity r1e3 = new Entity();
		r1e3.add(new PositionComponent(10, 3, DirectionType.Left));
		r1e3.add(new RenderComponent(Constants.PIG_FORM));
		r1e3.add(new CollisionComponent(ColliderType.character));
		r1e3.add(new AIComponent(new BasicMeleeAI(r1e3, engine)));
		Array<Ability> abilities_r1e3 = new Array<Ability>();
		abilities_r1e3.add(Constants.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE));
		r1e3.add(new AbilityComponent(abilities_r1e3, engine));
		r1e3.add(new StatComponent("Close_Enemy_2", 25f, 60f, Color.YELLOW, 2f, 2f, 1.5f));
		r1entities.add(r1e3);
		
//		Entity r1e4 = new Entity();
//		r1e4.add(new PositionComponent(5, 5, DirectionType.Left));
//		r1e4.add(new RenderComponent(Constants.ICE_FORM));
//		r1e4.add(new CollisionComponent(ColliderType.character));
//		r1e4.add(new AIComponent(new BasicRangeAI(r1e4, engine)));
//		Array<Ability> abilities_r1e4 = new Array<Ability>();
//		abilities_r1e4.add(Constants.abilityMap.get(AbilityNameType.AT_WAVE_OF_FIRE));
//		abilities_r1e4.add(Constants.abilityMap.get(AbilityNameType.DF_TELEPORT));
//		r1e4.add(new AbilityComponent(abilities_r1e4, engine));
//		r1e4.add(new StatComponent("Range_Enemy_2", 25f, 60f, Color.YELLOW, 2f, 2f, 1.3f));
//		r1entities.add(r1e4);

		Round r1 = new Round(r1entities);
		rounds.add(r1);
		
		//ROUND 2
		Array<Entity> r2entities = new Array<Entity>(1);
		//ENTITIES
		Entity r2e1 = new Entity();
		r2e1.add(new PositionComponent(11, 5, DirectionType.Left));
		r2e1.add(new RenderComponent(Constants.ICE_FORM));
		r2e1.add(new CollisionComponent(ColliderType.character));
		r2e1.add(new AIComponent(new BasicRangeAI(r2e1, engine)));
		Array<Ability> abilities_r2e1 = new Array<Ability>();
		abilities_r2e1.add(Constants.abilityMap.get(AbilityNameType.AT_WAVE_OF_FIRE));
		abilities_r2e1.add(Constants.abilityMap.get(AbilityNameType.DF_TELEPORT));
		r2e1.add(new AbilityComponent(abilities_r2e1, engine));
		r2e1.add(new StatComponent("Range_Enemy_1", 25f, 60f, Color.YELLOW, 2f, 2f, 1.3f));
		r2entities.add(r2e1);
		
		Round r2 = new Round(r2entities);
		rounds.add(r2);
		
		//ROUND 3
		Array<Entity> r3entities = new Array<Entity>(1);
		//ENTITIES
		Entity r3e1 = new Entity();
		r3e1.add(new PositionComponent(11, 5, DirectionType.Left));
		r3e1.add(new RenderComponent(Constants.ICE_FORM));
		r3e1.add(new CollisionComponent(ColliderType.character));
		r3e1.add(new AIComponent(new BasicMeleeAI(r3e1, engine)));
		Array<Ability> abilities_r3e1 = new Array<Ability>();
		abilities_r3e1.add(Constants.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE));
		r3e1.add(new AbilityComponent(abilities_r3e1, engine));
		r3e1.add(new StatComponent("Enemy3", 45f, 50f, Color.YELLOW, 2f, 2f, 1.3f));
		r3entities.add(r3e1);
		
		Entity r3e2 = new Entity();
		r3e2.add(new PositionComponent(2, 8, DirectionType.Left));
		r3e2.add(new RenderComponent(Constants.ICE_FORM));
		r3e2.add(new CollisionComponent(ColliderType.character));
		r3e2.add(new AIComponent(new BasicMeleeAI(r3e2, engine)));
		Array<Ability> abilities_r3e2 = new Array<Ability>();
		abilities_r3e2.add(Constants.abilityMap.get(AbilityNameType.AT_BLADE_STRIKE));
		r3e2.add(new AbilityComponent(abilities_r3e2, engine));
		r3e2.add(new StatComponent("Enemy3", 45f, 50f, Color.YELLOW, 2f, 2f, 1.3f));
		r3entities.add(r3e2);
		
		Round r3 = new Round(r3entities);
		rounds.add(r3);
		
	}

	@Override
	public void entityAdded(Entity entity) {
		
	}

	@Override
	public void entityRemoved(Entity entity) {
		Array<Entity> enemies = rounds.get(currentRound).entities;
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
				Play.levelManager.goToLevel(LevelType.CARAVAN_LEVEL);
			}else {
				addEntities();
				StatComponent stat = Mappers.staMap.get(player);
				stat.health = stat.max_health;
				stat.energy = stat.max_energy;
			}
		}
	}

	@Override
	public void transitionIn(float deltaTime) {
		engine.addEntityListener(this);
		Play.levelManager.enterLevel();
	}

	@Override
	public void transitionOut(float deltaTime) {
		engine.removeEntityListener(this);
		Play.levelManager.leaveLevel();
	}
}
