package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.defense.Blade_Block;

public class PerfectBlock extends Upgrade {

	private static final int[] cost = {3};
	private Blade_Block ability;
	
	public PerfectBlock(Blade_Block ability) {
		super("Perfect Block", cost);
		
		this.ability = ability;
	}

	@Override
	protected void handleUpgrade() {
		ability.setCanPerfectBlock(true);
	}

}
