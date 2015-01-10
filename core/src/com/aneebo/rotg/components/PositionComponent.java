package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent extends Component {

	public int pXPos, pYPos;
	public float curXPos, curYPos;
	public int nXPos, nYPos;
	
	public PositionComponent(int x, int y) {
		curXPos = x;
		curYPos = y;
		nXPos = x;
		nYPos = y;
		pXPos = x;
		pYPos = y;
	}
	
	public boolean isStopped() {
		return curXPos==nXPos && curYPos==nYPos;
	}
}
