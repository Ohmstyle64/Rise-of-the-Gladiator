package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.offense.Blade_Strike;

public class PowerStrike extends Upgrade {

	private static final int[] cost = {3,6,9};
	private Blade_Strike ability;
	
	public PowerStrike(Blade_Strike ability) {
		super("Power Strike", cost);
		
		this.ability = ability;
	}
	
	public PowerStrike(Blade_Strike ability, int[] cost) {
		super("Power Strike", cost);
		
		this.ability = ability;
	}

	@Override
	protected void handleUpgrade() {
		ability.setActRange(ability.getRange()+rank+1);
	}

}
