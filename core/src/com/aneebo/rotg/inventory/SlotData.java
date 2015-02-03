package com.aneebo.rotg.inventory;

public class SlotData {
	public Inventory inv;
	public Item item;
	public int location;
	
	public SlotData(Inventory inv, Item item, int location) {
		this.inv = inv;
		this.item = item;
		this.location = location;
	}

	public SlotData(SlotData userObject) {
		this.inv = userObject.inv;
		this.item = userObject.item;
		this.location = userObject.location;
	}
	
	public void set(SlotData userObject) {
		this.inv = userObject.inv;
		this.item = userObject.item;
		this.location = userObject.location;
	}
}
