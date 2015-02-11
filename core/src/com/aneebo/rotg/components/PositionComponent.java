package com.aneebo.rotg.components;

import com.aneebo.rotg.types.DirectionType;
import com.badlogic.ashley.core.Component;

public class PositionComponent extends Component {
	public DirectionType direction;
	public float curXPos, curYPos;
	public int nXPos, nYPos;
	public boolean isMoveable;
	
	public PositionComponent(int x, int y, DirectionType direction) {
		curXPos = x;
		curYPos = y;
		nXPos = x;
		nYPos = y;
		isMoveable = true;
		this.direction = direction;
	}
	
	public PositionComponent() {
		isMoveable = true;
	}
	
	public boolean isStopped() {
		return curXPos==nXPos && curYPos==nYPos;
	}
}
