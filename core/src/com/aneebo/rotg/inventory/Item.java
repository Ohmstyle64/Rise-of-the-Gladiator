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
	
	public abstract float getDamageMitigation();
	
	public abstract float getMagicResist();
	
	public abstract float getIncreaseToRange();

	public abstract float getAttackSpeed();
	
	public abstract float getDamage();
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Item) {
			return ((Item)obj).slot == slot;
		}
		return super.equals(obj);
	}
	
}
