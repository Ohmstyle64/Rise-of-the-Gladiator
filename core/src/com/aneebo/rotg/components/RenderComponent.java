package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class RenderComponent extends Component {
	
	public Texture texture;

	public RenderComponent(Texture texture) {
		this.texture = texture;
	}

}
