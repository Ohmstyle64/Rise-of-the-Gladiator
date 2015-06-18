package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;

public class RenderComponent extends Component {
	
	public String textureName;
	public boolean shake;
	public float shakeFreq;
	public int frames;
	public int frame_limit;
	
	public RenderComponent() {
		
	}

	public RenderComponent(String textureName) {
		this.textureName = textureName;
		shake = false;
		frames = 0;
		frame_limit = 0;
	}

	public void flashRed(int numFrames) {
		frames = 0;
		frame_limit = numFrames;
	}
	
	public void reset() {
		frames = 0;
		frame_limit = 0;
	}
}
