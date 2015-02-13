package com.aneebo.rotg.inventory;

import java.util.Iterator;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.inventory.items.EmptyItem;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class Inventory {
	public ObjectMap<Integer, Item> equipped;
	public Array<Item> inventoryList;
	public Item selected;
	public ImageButton btnSelected;
		
	private Entity me;
	private StatComponent stat;
	
	private EmptyItem blank;
	
	public Inventory(Entity me, ObjectMap<Integer, Item> equipped, Array<Item> inventoryList) {
		this.me = me;
		this.equipped = equipped;
		this.inventoryList = inventoryList;
		selected = null;
		btnSelected = null;
		blank = new EmptyItem();
		updateStats();
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
	
	public void updateStats() {
		stat = Mappers.staMap.get(me);
		Iterator<Entry<Integer, Item>> it = equipped.iterator();
		while(it.hasNext()) {
			Entry<Integer, Item> ent = it.next();
			stat.damageMitigation += ent.value.getDamageMitigation();
			stat.increaseToAttackSpeed += ent.value.getAttackSpeed();
			stat.increaseToDamage += ent.value.getDamage();
			stat.increaseToRange += ent.value.getIncreaseToRange();
			stat.magicResist += ent.value.getMagicResist();
		}
	}
}
