package com.aneebo.rotg.systems;

import com.aneebo.rotg.abilities.Ability;
import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.InventoryComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.ItemSlotListener;
import com.aneebo.rotg.inventory.SlotData;
import com.aneebo.rotg.inventory.items.EmptyItem;
import com.aneebo.rotg.types.DirectionType;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class InputSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	
	private Array<Ability> abilitySlots;
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private InventoryComponent invC;
	private InputComponent inputComponent;
	private Entity player;
	
	private Table equippedTable;
	private Table inventoryTable;
	private Table mainTable;
	private ItemSlotListener itemSlotListener;
	
	private Stage stage;
	private Skin skin;
	private Window win;

	public InputSystem(Stage stage) {
		super(0);
		this.stage = stage;
		this.skin = Assets.assetManager.get(Constants.UI_SKIN, Skin.class);
		itemSlotListener = new ItemSlotListener();
	}
	
	private void createInventoryWindow() {
		win = new Window("Character Screen", skin);
		invC = Mappers.invMap.get(player);
		mainTable = new Table();
		TextButton closeBtn = new TextButton("Close", skin);
		closeBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				win.setVisible(false);
			}
		});
		mainTable.add(createHeader()).colspan(2).row();
		mainTable.add(createEquipped());
		mainTable.add(createInventory()).row();
		mainTable.add(closeBtn).colspan(2);
		win.add(mainTable);
		win.setPosition(Constants.WIDTH / 2 - win.getWidth() / 2, Constants.HEIGHT - win.getHeight());
		win.pack();
		win.setVisible(false);
		stage.addActor(win);
	}

	private void refreshData() {
		inventoryTable.clear();
		int cols = 4;
		Array<Item> iList = invC.inventory.inventoryList;
		for(int i = 0; i < Constants.INVENTORY_SIZE; i++) {
			ImageButton btn = new ImageButton(iList.get(i).icon);
			btn.addListener(itemSlotListener);
			btn.setUserObject(new SlotData(invC.inventory,iList.get(i),Constants.INVENTORY));
			inventoryTable.add(btn);
			if(i % cols == cols - 1) inventoryTable.row();
		}
		
		equippedTable.clear();
		ObjectMap<Integer, Item> eq = invC.inventory.equipped;
		ImageButton headBtn;
		//Head Slot
		if(eq.containsKey(Constants.HEAD)) {
			headBtn = new ImageButton(eq.get(Constants.HEAD).icon);
			headBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.HEAD), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.HEAD);
			eq.put(empty.slot, empty);
			headBtn = new ImageButton(empty.icon);
			headBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		headBtn.addListener(itemSlotListener);
		
		//Body Slot
		ImageButton bodyBtn;
		if(eq.containsKey(Constants.BODY)) {
			bodyBtn = new ImageButton(eq.get(Constants.BODY).icon);
			bodyBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.BODY), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.BODY);
			eq.put(empty.slot, empty);
			bodyBtn = new ImageButton(empty.icon);
			bodyBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		bodyBtn.addListener(itemSlotListener);
		
		//Primary Slot
		ImageButton primBtn;
		if(eq.containsKey(Constants.PRIMARY)) {
			primBtn = new ImageButton(eq.get(Constants.PRIMARY).icon);
			primBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.PRIMARY), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.PRIMARY);
			eq.put(empty.slot, empty);
			primBtn = new ImageButton(empty.icon);
			primBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		primBtn.addListener(itemSlotListener);
		
		equippedTable.add(headBtn).row();
		equippedTable.add(bodyBtn).row();
		equippedTable.add(primBtn);
		mainTable.invalidateHierarchy();
	}

	
	private Table createInventory() {
		inventoryTable = new Table();
		int cols = 4;
		Array<Item> iList = invC.inventory.inventoryList;
		for(int i = 0; i < Constants.INVENTORY_SIZE; i++) {
			ImageButton btn = new ImageButton(iList.get(i).icon);
			btn.addListener(itemSlotListener);
			btn.setUserObject(new SlotData(invC.inventory,iList.get(i),Constants.INVENTORY));
			inventoryTable.add(btn);
			if(i % cols == cols - 1) inventoryTable.row();
		}
		
		return inventoryTable;
	}

	private Table createEquipped() {
		equippedTable = new Table();
		ObjectMap<Integer, Item> eq = invC.inventory.equipped;
		ImageButton headBtn;
		//Head Slot
		if(eq.containsKey(Constants.HEAD)) {
			headBtn = new ImageButton(eq.get(Constants.HEAD).icon);
			headBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.HEAD), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.HEAD);
			eq.put(empty.slot, empty);
			headBtn = new ImageButton(empty.icon);
			headBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		headBtn.addListener(itemSlotListener);
		
		//Body Slot
		ImageButton bodyBtn;
		if(eq.containsKey(Constants.BODY)) {
			bodyBtn = new ImageButton(eq.get(Constants.BODY).icon);
			bodyBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.BODY), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.HEAD);
			eq.put(empty.slot, empty);
			bodyBtn = new ImageButton(empty.icon);
			bodyBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		bodyBtn.addListener(itemSlotListener);
		
		//Primary Slot
		ImageButton primBtn;
		if(eq.containsKey(Constants.PRIMARY)) {
			primBtn = new ImageButton(eq.get(Constants.PRIMARY).icon);
			primBtn.setUserObject(new SlotData(invC.inventory, eq.get(Constants.PRIMARY), Constants.EQUIPPED));
		}else {
			EmptyItem empty = new EmptyItem(Constants.PRIMARY);
			eq.put(empty.slot, empty);
			primBtn = new ImageButton(empty.icon);
			primBtn.setUserObject(new SlotData(invC.inventory, empty, Constants.EQUIPPED));
		}
		primBtn.addListener(itemSlotListener);
		
		equippedTable.add(headBtn).row();
		equippedTable.add(bodyBtn).row();
		equippedTable.add(primBtn);
		return equippedTable;
	}

	private Table createHeader() {
		Table table = new Table();
		Label name = new Label("Name: "+Mappers.staMap.get(player).name, skin);
		Label health = new Label("Health: "+Mappers.staMap.get(player).max_health, skin);
		Label energy = new Label("Energy: "+Mappers.staMap.get(player).max_energy, skin);
		
		table.add(name).row();
		table.add(health).row();
		table.add(energy);
		return table;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(InputComponent.class, AbilityComponent.class).get());
		player = entities.first();
		posComponent = Mappers.posMap.get(player);
		abilityComponent = Mappers.abMap.get(player);
		inputComponent = Mappers.inpMap.get(player);
		createInventoryWindow();
	}

	@Override
	public void update(float deltaTime) {
		//SINGLEPLAYER ONLY
		abilitySlots = abilityComponent.abilitySlots;
		if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
			if(abilitySlots.get(0) instanceof Ability) {
				if(posComponent.isStopped())
					abilitySlots.get(0).isActivated = true;
			}else
				abilitySlots.get(0).isActivated = true;
		}else if(Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
			abilitySlots.get(1).isActivated = true;
		}
		
		if(!posComponent.isStopped()) return;
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			posComponent.direction = DirectionType.Left;
			if(posComponent.isMoveable)
				posComponent.gridNXPos--;
		}else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			posComponent.direction = DirectionType.Right;
			if(posComponent.isMoveable)
				posComponent.gridNXPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.W)) {
			posComponent.direction = DirectionType.Up;
			if(posComponent.isMoveable)
				posComponent.gridNYPos++;
		}else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			posComponent.direction = DirectionType.Down;
			if(posComponent.isMoveable)
				posComponent.gridNYPos--;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.C)) {
			win.setVisible(!win.isVisible());
		}

		if(inputComponent.needRefresh) {
			refreshData();
			inputComponent.needRefresh = false;
		}
	}

	public void dispose() {
	}

}
