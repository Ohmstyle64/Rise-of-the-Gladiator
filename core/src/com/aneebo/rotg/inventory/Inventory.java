package com.aneebo.rotg.inventory;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class Inventory {
	public ObjectMap<Integer, Item> equipped;
	
	public Inventory(ObjectMap<Integer, Item> equipped) {
		this.equipped = equipped;
	}
	
	public void dipose() {
		Iterator<Entry<Integer, Item>> it = equipped.iterator();
		while(it.hasNext()) {
			Entry<Integer, Item> entry = it.next();
			entry.value.icon.dispose();
		}
	}
	
}
