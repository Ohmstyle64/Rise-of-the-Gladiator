package com.aneebo.rotg.components;

import com.aneebo.rotg.types.AIState;
import com.badlogic.ashley.core.Component;


public class AIComponent extends Component {
	public AIState aiState;
	public AIComponent(AIState aiState) {
		this.aiState = aiState;
	}
}
