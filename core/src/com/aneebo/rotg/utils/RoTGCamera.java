package com.aneebo.rotg.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.RandomXS128;

public class RoTGCamera extends OrthographicCamera {
	
	  public float time;
	  private float x, y, current_time, power, current_power;
	  private RandomXS128 random;
	  private float storedX, storedY;
	
	public RoTGCamera() {
		super();
	    time = 0;
	    current_time = 0;
	    power = 0;
	    current_power = 0;
	    random = new RandomXS128();
	}
	
	public void shake(float power, float time) {
		storedX = this.position.x;
		storedY = this.position.y;
		this.power = power;
		this.time = time;
		this.current_time = 0;
	}
	
	public void smallShake() {
		shake(1f, .1f);
	}
	
	public void mediumShake() {
		shake(2f, .1f);
	}
	
	public void largeShake() {
		shake(5f, .1f);
	}
	
	public void update(float deltaTime) {
		if(current_time <= time) {
			current_power = power * ((time - current_time) / time);
			
			x = (random.nextFloat() - 0.5f) * 2 * current_power;
			y = (random.nextFloat() - 0.5f) * 2 * current_power;
			
			this.translate(-x, -y);
			current_time += deltaTime;
			this.update();
		}else {
			this.position.x = storedX;
			this.position.y = storedY;
			this.update();
		}
	}
}
