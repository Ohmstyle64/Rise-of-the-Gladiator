package com.aneebo.rotg.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Inventory {
	public ObjectMap<Integer, Item> equipped;
	public Array<Item> inventoryList;
	public Item selected;
	public ImageButton btnSelected;
	
	public Inventory(ObjectMap<Integer, Item> equipped, Array<Item> inventoryList) {
		this.equipped = equipped;
		this.inventoryList = inventoryList;
		selected = null;
		btnSelected = null;
	}
	
	public boolean isEquipped(Item item) {
		return equipped.containsValue(item, true);
	}
	
	public boolean isInInventory(Item item) {
		return inventoryList.contains(item, true);
	}
}
