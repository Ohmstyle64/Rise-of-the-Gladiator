package com.aneebo.rotg.inventory;

import com.aneebo.rotg.inventory.items.EmptyItem;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Inventory {
	public ObjectMap<Integer, Item> equipped;
	public Array<Item> inventoryList;
	public Item selected;
	public ImageButton btnSelected;
	
	private EmptyItem blank;
	
	public Inventory(ObjectMap<Integer, Item> equipped, Array<Item> inventoryList) {
		this.equipped = equipped;
		this.inventoryList = inventoryList;
		selected = null;
		btnSelected = null;
		blank = new EmptyItem();
	}
	
	public boolean isEquipped(Item item) {
		return equipped.containsValue(item, true);
	}
	
	public boolean isInInventory(Item item) {
		return inventoryList.contains(item, true);
	}
	
	public void addItemToInventory(Item itemToAdd) {
		int firstBlank = inventoryList.indexOf(blank, false);
		if(firstBlank > -1) {
			inventoryList.removeIndex(firstBlank);
			inventoryList.insert(firstBlank, itemToAdd);
		}else {
			System.out.println("Inventory is Full!");
		}
	}
	
	public void addItemsToInventory(Array<Item> itemsToAdd) {
		for(Item i : itemsToAdd) {
			addItemToInventory(i);
		}
	}
}
