package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;

public class MovementComponent extends Component {

	public float curXPos, curYPos;
	public int nXPos, nYPos;
	
	public MovementComponent(int x, int y) {
		curXPos = x;
		curYPos = y;
		nXPos = x;
		nYPos = y;
	}
	
	public boolean isStopped() {
		return curXPos==nXPos && curYPos==nYPos;
	}
}
