package com.aneebo.rotg.components;

import com.aneebo.rotg.types.LevelType;
import com.badlogic.ashley.core.Component;

public class LevelChangerComponent extends Component {
	public LevelType level;
	
	public LevelChangerComponent(LevelType level) {
		this.level = level;
	}
}
