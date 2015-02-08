package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;

public class LevelChangerComponent extends Component {
	public int level;
	
	public LevelChangerComponent(int level) {
		this.level = level;
	}
}
