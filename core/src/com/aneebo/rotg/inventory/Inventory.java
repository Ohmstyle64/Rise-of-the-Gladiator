package com.aneebo.rotg.inventory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Inventory {
	public ObjectMap<Integer, Item> equipped;
	public Array<Item> inventoryList;
	public Item selected;
	
	public Inventory(ObjectMap<Integer, Item> equipped, Array<Item> inventoryList) {
		this.equipped = equipped;
		this.inventoryList = inventoryList;
		selected = null;
	}
}
