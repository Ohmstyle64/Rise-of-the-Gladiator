package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;

public class RenderComponent extends Component {
	
	public String textureName;
	public String textToDisplay;
	public boolean needsToDisplay;

	public RenderComponent(String textureName) {
		this.textureName = textureName;
		needsToDisplay = false;
	}

}
