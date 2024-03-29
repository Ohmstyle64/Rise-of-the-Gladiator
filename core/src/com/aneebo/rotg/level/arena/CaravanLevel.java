package com.aneebo.rotg.level.arena;

import com.aneebo.rotg.components.CollisionComponent;
import com.aneebo.rotg.components.LevelChangerComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.MerchantComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.Inventory;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.items.ChestP1;
import com.aneebo.rotg.level.Level;
import com.aneebo.rotg.level.LevelManager;
import com.aneebo.rotg.types.ColliderType;
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.types.LevelType;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class CaravanLevel extends Level {

	public CaravanLevel(Engine engine, Entity player, Vector2 playerStart, LevelManager manager) {
		super(engine, player, Constants.TEST_MAP, playerStart, manager);
	}


	@Override
	protected void loadLevel() {
		currentRound = 0;
		totalRounds = 1;
		Array<Entity> entities = new Array<Entity>(2);
		Entity merchant = new Entity();
		merchant.add(new PositionComponent(9,5,DirectionType.Right));
		merchant.add(new RenderComponent(Constants.DRAGON_FORM));
		merchant.add(new StatComponent("Merchant", "Male", 1f, 1f, Color.WHITE, 1f, 1f, 0f, false));
		merchant.add(new CollisionComponent(ColliderType.merchant));
		Array<Item> iList = new Array<Item>(true, Constants.INVENTORY_SIZE, Item.class);
		iList.add(new ChestP1());
		ObjectMap<Integer, Item> e = new ObjectMap<Integer, Item>();
		Inventory inv = new Inventory(merchant, e, fillRest(iList));
		String[] dialogue = new String[2];
		dialogue[0] = "Hello, welcome to my store!";
		dialogue[1] = "Have a nice day! Come again..";
		merchant.add(new MerchantComponent(inv, dialogue));
		
		Entity changer = new Entity();
		changer.add(new PositionComponent(9,7,DirectionType.Down));
		changer.add(new RenderComponent(Constants.ICE_FORM));
		changer.add(new StatComponent("Change Level", "Male", 1f, 1f, Color.WHITE, 1f, 1f, 0f, false));
		changer.add(new CollisionComponent(ColliderType.levelChange));
		changer.add(new LevelChangerComponent(LevelType.TEST_LEVEL));
		
		entities.add(merchant);
		entities.add(changer);
		
		Round r = new Round(entities);
		
		rounds.add(r);
		
		PositionComponent pos = Mappers.posMap.get(player);
		pos.curXPos = playerStart.x;
		pos.curYPos = playerStart.y;
		pos.gridNXPos = (int) playerStart.x;
		pos.gridNYPos = (int) playerStart.y;
		pos.gridCurXPos = (int) playerStart.x;
		pos.gridCurYPos = (int) playerStart.y;
	}

	@Override
	public void transitionIn(float deltaTime) {
		manager.enterLevel();
	}


	@Override
	public void transitionOut(float deltaTime) {
		manager.leaveLevel();
	}
}
