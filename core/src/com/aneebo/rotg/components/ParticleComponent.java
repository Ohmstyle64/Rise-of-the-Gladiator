package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleComponent extends Component {
	
	public ParticleEffect pEffect;
	
	public ParticleComponent(ParticleEffect pEffect) {
		this.pEffect = pEffect;
	}
}
