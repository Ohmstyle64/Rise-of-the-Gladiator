package com.aneebo.rotg.abilities.upgrades;


public abstract class Upgrade {
	protected String name;
	private int[] cost;
	protected int rank;
	
	public Upgrade(String name, int[] cost) {
		this.name = name;
		this.cost = cost;
		rank = -1;
	}
	
	public int increaseUpgradeLevel(int pts) {
		if(rank==cost.length-1) return pts;
		int size = cost.length;
		for(int i = rank; i < size; i++) {
			if(pts >= cost[i]) {
				pts -= cost[i];
				rank++;
				handleUpgrade();
			}
		}
		return pts;
	}
	
	protected abstract void handleUpgrade();
	
	public String getName() {
		return name;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getSpentCost() {
		int spent = 0;
		
		if(rank==-1) return 0;
		
		for(int i = 0; i < rank; i++) {
			spent += cost[i];
		}
		
		return spent;
	}
	
}
