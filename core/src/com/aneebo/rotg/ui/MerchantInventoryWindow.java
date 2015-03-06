package com.aneebo.rotg.ui;

import com.aneebo.rotg.components.MerchantComponent;
import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.ItemSlotListener;
import com.aneebo.rotg.inventory.SlotData;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class MerchantInventoryWindow extends Window {

	private Table mainTable;
	private Table myInventoryTable;
	private Table myHeaderTable;
	
	private MerchantComponent merchantComponent;
	private ItemSlotListener itemSlotListener;
	
	public MerchantInventoryWindow(String title, Skin skin, MerchantComponent merchantComponent) {
		super(title, skin);
		this.merchantComponent = merchantComponent;
		itemSlotListener = new ItemSlotListener();
		mainTable = new Table(skin);
		myInventoryTable = new Table(skin);
		myHeaderTable = new Table(skin);
		TextButton closeBtn = new TextButton("Close", skin);
		closeBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MerchantInventoryWindow.this.setVisible(false);
				MerchantInventoryWindow.this.merchantComponent.isSelling = false;
			}
		});
		
		createHeader();
		createInventory();
		
		mainTable.add(myHeaderTable).colspan(2).row();
		mainTable.add(myInventoryTable).row();
		mainTable.add(closeBtn).colspan(2);
		add(mainTable);
		setPosition(Constants.WIDTH / 2 - getWidth() / 2, Constants.HEIGHT - getHeight());
		pack();
	}

	private void createInventory() {
		int cols = 4;
		Array<Item> iList = merchantComponent.inventory.inventoryList;
		for(int i = 0; i < Constants.INVENTORY_SIZE; i++) {
			ImageButton btn = new ImageButton(iList.get(i).icon);
			btn.addListener(itemSlotListener);
			btn.setUserObject(new SlotData(merchantComponent.inventory,iList.get(i),Constants.INVENTORY));
			myInventoryTable.add(btn);
			if(i % cols == cols - 1) myInventoryTable.row();
		}
	}

	private void createHeader() {
		
	}

	public void setEntity(Entity me) {
		createInventory();
		createHeader();
		invalidateHierarchy();
	}
}
