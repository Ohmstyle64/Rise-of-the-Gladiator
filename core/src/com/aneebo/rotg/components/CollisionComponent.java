package com.aneebo.rotg.components;

import com.aneebo.rotg.types.ColliderType;
import com.badlogic.ashley.core.Component;

public class CollisionComponent extends Component {
	public ColliderType type;
	
	public CollisionComponent(ColliderType type) {
		this.type = type;
	}
	
	public CollisionComponent() {
		
	}
}
