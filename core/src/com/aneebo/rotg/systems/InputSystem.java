package com.aneebo.rotg.systems;

import java.util.Iterator;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.InventoryComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.ItemSlotListener;
import com.aneebo.rotg.inventory.SlotData;
import com.aneebo.rotg.types.Direction;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class InputSystem extends EntitySystem {

	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<StatComponent> sc = ComponentMapper.getFor(StatComponent.class);
	private ComponentMapper<InventoryComponent> ic = ComponentMapper.getFor(InventoryComponent.class);
	private ImmutableArray<Entity> entities;
	
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private InventoryComponent invC; 
	private Entity player;
	
	private Stage stage;
	private Skin skin;
	private Window win;

	//Empty Cell
	private TextureRegionDrawable emptyRegion = new TextureRegionDrawable(
			new TextureRegion(Assets.assetManager.get(Constants.EMPTY_CELL, Texture.class)));
	
	public InputSystem(Stage stage, Skin skin) {
		super(0);
		this.stage = stage;
		this.skin = skin;
	}
	
	private void createInventoryWindow() {
		win = new Window("Character Screen", skin);
		Table table = new Table();
		invC = ic.get(player);
		
		TextButton closeBtn = new TextButton("Close", skin);
		closeBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				win.setVisible(false);
			}
		});
		table.add(createHeader()).colspan(2).row();
		table.add(createEquipped());
		table.add(createInventory()).row();
		table.add(closeBtn).colspan(2);
		win.add(table);
		win.setPosition(Constants.WIDTH / 2 - win.getWidth() / 2, Constants.HEIGHT - win.getHeight());
		win.pack();
		win.setVisible(false);
		stage.addActor(win);
	}

	private Table createInventory() {
		Table table = new Table();
		int cols = 4;
		int rows = MathUtils.ceil(Constants.INVENTORY_SIZE / cols);
		int size = cols * rows;
		Array<Item> iList = invC.inventory.inventoryList;
		for(int i = 0; i < size; i++) {
			ImageButton btn;
			if(iList.size <= i ) {
				btn = new ImageButton(emptyRegion);
			}else {
				btn = new ImageButton(iList.get(i).icon);
			}
			btn.addListener(new ItemSlotListener());
			btn.setUserObject(new SlotData(invC.inventory,((i >= iList.size ? null : iList.get(i)))));
			table.add(btn);
			if(i % cols == cols - 1) table.row();
		}
		
		return table;
	}

	private Table createEquipped() {
		Table table = new Table();
		
		
		Iterator<Entry<Integer, Item>> it = invC.inventory.equipped.iterator();
		while(it.hasNext()) {
			Entry<Integer, Item> ent = it.next();
			TextureRegionDrawable region = invC.inventory.equipped.get(ent.key).icon;
			ImageButton btn = new ImageButton(region);
			btn.addListener(new ItemSlotListener());
			btn.setUserObject(new SlotData(invC.inventory,ent.value));
			table.add(btn).row();
		}
		
		return table;
	}

	private Table createHeader() {
		Table table = new Table();
		Label name = new Label("Name: "+sc.get(player).name, skin);
		Label health = new Label("Health: "+sc.get(player).max_health, skin);
		Label energy = new Label("Energy: "+sc.get(player).max_energy, skin);
		
		table.add(name).row();
		table.add(health).row();
		table.add(energy);
		return table;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(InputComponent.class, AbilityComponent.class));
		player = entities.first();
		posComponent = pc.get(player);
		abilityComponent = ac.get(player);
		createInventoryWindow();
	}

	@Override
	public void update(float deltaTime) {
		//SINGLEPLAYER ONLY
		abilitySlots = abilityComponent.abilitySlots;
		if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
			abilitySlots.get(0).isActivated = true;
		}else if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
			abilitySlots.get(1).isActivated = true;
		}
		
		if(!posComponent.isStopped()) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			posComponent.direction = Direction.Left;
			if(posComponent.isMoveable)
				posComponent.nXPos--;
		}else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			posComponent.direction = Direction.Right;
			if(posComponent.isMoveable)
				posComponent.nXPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.W)) {
			posComponent.direction = Direction.Up;
			if(posComponent.isMoveable)
				posComponent.nYPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			posComponent.direction = Direction.Down;
			if(posComponent.isMoveable)
				posComponent.nYPos--;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.C)) {
			win.setVisible(!win.isVisible());
		}
	}

	public void dispose() {
	}

}
