package com.aneebo.rotg.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Item {
	public String name;
	public TextureRegionDrawable icon;
	public int slot;
	public boolean isSelected;
	
	public Item(String name, Texture icon, int slot) {
		this.name = name;
		this.icon = new TextureRegionDrawable(
				new TextureRegion(icon));
		this.slot = slot;
		isSelected = false;
	}
}
