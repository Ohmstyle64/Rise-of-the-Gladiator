package com.aneebo.rotg.inventory;

import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ItemSlotListener extends ClickListener {

	//Empty Cell
	private TextureRegionDrawable emptyRegion = new TextureRegionDrawable(
			new TextureRegion(Assets.assetManager.get(Constants.EMPTY_CELL, Texture.class)));
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		ImageButton btn = (ImageButton)event.getListenerActor();
		SlotData data = (SlotData)btn.getUserObject();
		if(data.item != null) {
			boolean isSelected = data.item.isSelected;
			if(isSelected) {
				isSelected = false;
				btn.getStyle().imageUp = emptyRegion;
				data.inv.selected = null;
			}else {
				if(data.inv.selected != null) {
					
					btn.getStyle().imageUp = emptyRegion;
					isSelected = false;
					data.inv.selected = null;
				}else {
					isSelected = true;
					btn.getStyle().imageUp = data.item.icon;
					data.inv.selected = data.item;
				}
			}
		}else {
			
		}
	}
}
