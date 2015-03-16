package com.aneebo.rotg.types;

public enum DirectionType {
	Left(1), Right(0), Up(2), Down(3);
	
	private int val;
	
	DirectionType(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
}
