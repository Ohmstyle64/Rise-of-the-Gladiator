package com.aneebo.rotg.types;

public enum DirectionType {
	Left(1), Right(3), Up(0), Down(2);
	
	private int val;
	
	DirectionType(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
}
