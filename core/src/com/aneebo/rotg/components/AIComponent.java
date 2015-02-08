package com.aneebo.rotg.components;

import com.aneebo.rotg.ai.AiPlan;
import com.badlogic.ashley.core.Component;


public class AIComponent extends Component {
	public AiPlan plan;
	
	public AIComponent(AiPlan plan) {
		this.plan = plan;
	}
}
