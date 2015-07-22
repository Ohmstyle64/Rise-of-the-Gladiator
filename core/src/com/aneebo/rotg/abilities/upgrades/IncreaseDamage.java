package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.Ability;

public class IncreaseDamage extends Upgrade {

	private static final int[] RANK_COST = {1,2,3,6,9};
	private Ability ability;
	
	public IncreaseDamage(Ability ability) {
		super("Increase Damage", RANK_COST);
		
		this.ability = ability;
	}
	
	public IncreaseDamage(Ability ability, int[] cost) {
		super("Increase Damage", cost);
		
		this.ability = ability;
	}

	@Override
	protected void handleUpgrade() {
		ability.setActDamage(ability.getDamage()*(1+(rank+1)*0.1f));
	}
}
