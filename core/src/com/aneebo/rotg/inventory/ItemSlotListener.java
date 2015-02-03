package com.aneebo.rotg.inventory;

import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ItemSlotListener extends ClickListener {

	@Override
	public void clicked(InputEvent event, float x, float y) {
		//TODO:THIS CAN BE IMPLEMENTED BETTER WITHOUT ALLOCATING NEW MEMORY FOR EACH SWAP
		ImageButton btn = (ImageButton)event.getListenerActor();
		SlotData data = (SlotData)btn.getUserObject();
		Inventory inv = data.inv;
		boolean isSelected = data.item.isSelected;
		
		if(isSelected) {	//SELECTING THE ITEM TWICE, SHOULD DESELECT
			isSelected = false;
			inv.selected = null;
		}else {		//THE CURRENT ITEM WAS NOT SELECTED
			if(inv.selected != null) {		//NEED TO SWAP THE PREVIOUSLY SELECTED ITEM WITH THIS ONE
				//NEED TO CHECK IF THIS IS A VALID SWAP
				if(true) {
					isSelected = false;
					if(inv.isEquipped(inv.selected) && inv.isInInventory(data.item)) {
						if(inv.selected.slot == data.item.slot || 
								data.item.slot == Constants.BLANK) {
							inv.inventoryList.add(inv.selected);
							if(data.item.slot == Constants.BLANK) {
								data.item.slot = inv.selected.slot;
								inv.selected.slot = Constants.BLANK;
							}
							inv.equipped.put(data.item.slot, data.item);
							inv.inventoryList.removeValue(data.item, true);
							
							SlotData temp = new SlotData((SlotData)btn.getUserObject());
							btn.setUserObject(inv.btnSelected.getUserObject());
							btn.getStyle().imageUp = inv.selected.icon;
							inv.btnSelected.setUserObject(temp);
							inv.btnSelected.getStyle().imageUp = temp.item.icon;
							temp = null;
						}else {
							System.out.println("Invalid Swap!");
						}
					}else if(inv.isInInventory(inv.selected) && inv.isInInventory(data.item)) {
						int first = inv.inventoryList.indexOf(inv.selected, true);
						int second = inv.inventoryList.indexOf(data.item, true);
						SlotData temp = new SlotData((SlotData)btn.getUserObject());
						btn.setUserObject(inv.btnSelected.getUserObject());
						btn.getStyle().imageUp = inv.selected.icon;
						inv.btnSelected.setUserObject(temp);
						inv.btnSelected.getStyle().imageUp = temp.item.icon;
						temp = null;
					}else if(inv.isInInventory(inv.selected) && inv.isEquipped(data.item)) {
						if(inv.selected.slot == data.item.slot || 
								inv.selected.slot == Constants.BLANK) {
							inv.inventoryList.add(data.item);
							if(inv.selected.slot == Constants.BLANK) {
								inv.selected.slot = data.item.slot;
								data.item.slot = Constants.BLANK;
							}
							inv.equipped.put(inv.selected.slot, inv.selected);
							inv.inventoryList.removeValue(inv.selected, true);
							
							SlotData temp = new SlotData((SlotData)inv.btnSelected.getUserObject());
							inv.btnSelected.setUserObject(btn.getUserObject());
							inv.btnSelected.getStyle().imageUp = data.item.icon;
							btn.setUserObject(temp);
							btn.getStyle().imageUp = inv.selected.icon;
							temp = null;
						}else {
							System.out.println("Invalid Swap!");
						}
					}else {
						System.out.println("Invalid operation!");
					}
					inv.selected.isSelected = false;
					inv.selected = null;
					inv.btnSelected = null;
				}
			}else {		//NO ITEM WAS SELECTED BEFORE THIS ONE
				isSelected = true;
				inv.selected = data.item;
				inv.btnSelected = btn;
			}
		}
	}
}
