package com.aneebo.rotg.components;

import com.aneebo.rotg.inventory.Inventory;
import com.badlogic.ashley.core.Component;

public class InventoryComponent extends Component {
	public Inventory inventory;
	
	public InventoryComponent(Inventory inventory) {
		this.inventory = inventory;
	}
}
