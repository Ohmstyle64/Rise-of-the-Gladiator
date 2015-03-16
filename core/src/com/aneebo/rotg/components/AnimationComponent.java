package com.aneebo.rotg.components;

import com.aneebo.rotg.utils.AnimationCollection;
import com.badlogic.ashley.core.Component;

public class AnimationComponent extends Component {
	
	public AnimationCollection animationCollection;
	
	public AnimationComponent(AnimationCollection animationCollection) {
		this.animationCollection = animationCollection;
	}
	
}
