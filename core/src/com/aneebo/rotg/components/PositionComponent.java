package com.aneebo.rotg.components;

import com.aneebo.rotg.types.DirectionType;
import com.badlogic.ashley.core.Component;

public class PositionComponent extends Component {
	public DirectionType direction;
	public float curXPos, curYPos;
	public int gridNXPos, gridNYPos, gridCurXPos, gridCurYPos;
	public boolean isMoveable;
	
	public PositionComponent(int x, int y, DirectionType direction) {
		curXPos = x;
		curYPos = y;
		gridNXPos = x;
		gridNYPos = y;
		gridCurXPos = x;
		gridCurYPos = y;
		isMoveable = true;
		this.direction = direction;
	}
	
	public PositionComponent() {
		isMoveable = true;
	}
	
	public boolean isStopped() {
		return gridCurXPos==gridNXPos && gridCurYPos==gridNYPos;
	}
	
	public void setCurPos(int x, int y) {
		curXPos = x;
		curYPos = y;
		gridNXPos = x;
		gridNYPos = y;
		gridCurYPos = y;
	}
	
	public void setCurXPos(int x) {
		curXPos = x;
		gridNXPos = x;
		gridCurXPos = x;
	}

	public void setCurYPos(int y) {
		curYPos = y;
		gridNYPos = y;
		gridCurYPos = y;
	}
}
