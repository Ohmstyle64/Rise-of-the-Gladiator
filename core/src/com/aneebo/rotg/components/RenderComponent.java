package com.aneebo.rotg.components;

import com.aneebo.rotg.utils.RoTGCamera;
import com.badlogic.ashley.core.Component;

public class RenderComponent extends Component {
	
	public String textureName;
	public RoTGCamera camera;
	public int frames;
	public int frame_limit;

	public RenderComponent(String textureName, RoTGCamera camera) {
		this.textureName = textureName;
		this.camera = camera;
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
