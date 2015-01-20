package com.aneebo.rotg.components;

import com.aneebo.rotg.types.Direction;
import com.badlogic.ashley.core.Component;

public class PositionComponent extends Component {
	public Direction direction;
	public float curXPos, curYPos;
	public int nXPos, nYPos;
	
	public PositionComponent(int x, int y, Direction direction) {
		curXPos = x;
		curYPos = y;
		nXPos = x;
		nYPos = y;
		this.direction = direction;
	}
	
	public boolean isStopped() {
		return curXPos==nXPos && curYPos==nYPos;
	}
}
