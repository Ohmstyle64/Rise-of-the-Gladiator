package com.aneebo.rotg.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FloatingTextManager {

	private FloatingTextPool ftp;
	private Array<FloatingTextMessage> messages;
	
	public FloatingTextManager() {
		ftp = new FloatingTextPool();
		messages = new Array<FloatingTextManager.FloatingTextMessage>();
	}
	
	public void addMessages(String text, float x, float y) {
		FloatingTextMessage message = ftp.obtain();
		message.init(text, x, y);
		messages.add(message);
	}
	
	public void update(float deltaTime) {
		for(FloatingTextMessage m : messages) {
			m.update(deltaTime);
		}
	}
		
	public void draw(Batch batch, BitmapFont font) {
		for(FloatingTextMessage m : messages) {
			m.draw(batch, font);
		}
	}
	
	private class FloatingTextPool extends Pool<FloatingTextMessage> {

		@Override
		protected FloatingTextMessage newObject() {
			return new FloatingTextMessage();
		}
		
	}
	
	private class FloatingTextMessage implements Poolable {
		
		private static final float SPEED = 10f;
		private static final float TIME = 3f;
		
		public String message;
		public float timer, x, y;
		
		public FloatingTextMessage() {
			this.message = "empty";
			timer = 0;
			x = 0; 
			y = 0;
		}
		
		public void init(String text, float x, float y) {
			this.message = text;
			this.x = x;
			this.y = y;
		}
		
		public void update(float deltaTime) {
			y+= deltaTime*SPEED;
			timer+=deltaTime;
			if(timer>= TIME) {
				messages.removeValue(this, true);
				ftp.free(this);
			}
		}
		
		public void draw(Batch batch, BitmapFont font) {
			font.draw(batch, message, x, y);
		}
		
		@Override
		public void reset() {
			timer = 0;
			x = 0;
			y = 0;
		}
	}
}
