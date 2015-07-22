package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.Ability;

public class DecreaseCost extends Upgrade {

	private static final int[] RANK_COST = {1,2,3,6,9};
	private Ability ability;
	
	public DecreaseCost(Ability ability) {
		super("Decrease Cost", RANK_COST);
		
		this.ability = ability;
		
	}
	
	public DecreaseCost(Ability ability, int[] cost) {
		super("Decrease Cost", cost);
		
		this.ability = ability;
	}

	@Override
	protected void handleUpgrade() {
		ability.setActEnergy_cost(ability.getEnergy_cost()*(1-(rank+1)*0.04f));
	}

}
