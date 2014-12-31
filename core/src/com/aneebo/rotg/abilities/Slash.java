package com.aneebo.rotg.abilities;

import com.aneebo.rotg.types.AbilityType;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Slash extends Ability {
	
	public Slash(int id, int castTime, int range, AbilityType type, String name) {
		super(id, castTime, range, type, name);
	}
	
	@Override
	protected void onLoopStart(float delta) {
		super.onLoopStart(delta);
		//ANY ADDITIONAL CODE SO START
		
	}
	
	@Override
	protected void onLoopEnd() {
		super.onLoopEnd();
		//ANY ADDITIONAL CODE TO END
		
	}
	
	@Override
	public void render(Batch batch) {
		if(timer >=castTime) {
			//DO ABILITY EFFECT
		}
	}

}
