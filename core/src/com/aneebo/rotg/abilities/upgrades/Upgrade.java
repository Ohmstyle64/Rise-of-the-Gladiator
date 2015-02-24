package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.Ability;

public abstract class Upgrade {
	protected String name;
	protected int[] cost;
	private int upgradeLevel;
	
	public Upgrade( String name, int[] cost) {
		this.name = name;
		this.cost = cost;
		upgradeLevel = 0;
	}
	
	public int increaseUpgradeLevel(Ability ability, int pts) {
		if(upgradeLevel==cost.length) return pts;
		int size = cost.length;
		for(int i = upgradeLevel; i < size; i++) {
			if(pts >= cost[i]) {
				pts -= cost[i];
				upgradeLevel++;
				handleUpgrade(ability);
			}
		}
		return pts;
	}
	
	protected abstract void handleUpgrade(Ability ability);
	
}
