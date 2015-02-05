package com.aneebo.rotg.level;

import com.aneebo.rotg.inventory.Item;
import com.badlogic.gdx.utils.Array;

public class Prize {
	public int skillPoints;
	public Array<Item> prize;
	public int currency;
	
	public Prize(int skillPoints, Array<Item> prize, int currency) {
		this.skillPoints = skillPoints;
		this.prize = prize;
		this.currency = currency;
	}
	
}
