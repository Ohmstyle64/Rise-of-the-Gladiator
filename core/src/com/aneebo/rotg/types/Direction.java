package com.aneebo.rotg.types;

public enum Direction {
	Left(1), Right(3), Up(0), Down(2);
	
	private int val;
	
	Direction(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
}
