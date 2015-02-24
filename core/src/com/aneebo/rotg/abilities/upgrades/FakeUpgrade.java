package com.aneebo.rotg.abilities.upgrades;

import com.aneebo.rotg.abilities.Ability;

public class FakeUpgrade extends Upgrade {

	private final static int[] bCost = new int[5];
	private final static String name = "fakename";

	public FakeUpgrade() {
		super(name, bCost);
	}

	@Override
	protected void handleUpgrade(Ability ability) {
		// TODO Auto-generated method stub
		
	}

}
