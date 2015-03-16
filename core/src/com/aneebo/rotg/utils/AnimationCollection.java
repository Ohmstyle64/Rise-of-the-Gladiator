package com.aneebo.rotg.utils;

import com.aneebo.rotg.types.DirectionType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class AnimationCollection {
	private String prevAnimation;
	private String currentAnimation;
	private int direction;
	private ObjectMap<String, AnimationHelper[]> aMap;
	
	public AnimationCollection(String currentAnimation) {
		this.currentAnimation = currentAnimation;
		this.prevAnimation = currentAnimation;
		aMap = new ObjectMap<String, AnimationHelper[]>();
	}
	
	public void addAnimation(String fileName, int anim_Width, int anim_Height, float frameDuration, float offsetX, float offsetY, DirectionType direction) {
		Texture anim_Texture = Assets.assetManager.get(fileName, Texture.class);
		anim_Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tr = new TextureRegion(anim_Texture);
		TextureRegion[][] textures = tr.split(anim_Width, anim_Height);
		TextureRegion[] idle = new TextureRegion[textures.length];
		AnimationHelper[] aHelper = new AnimationHelper[textures[0].length];
		for(int i = 0; i < textures.length; i++) {
			Array<TextureRegion> frames = new Array<TextureRegion>(textures[0].length - 1);
			for(int j = 0; j < textures[0].length; j++) {
				if(j == 0 ) {
					idle[i] = textures[i][j];
				}
				frames.add(textures[i][j]);
			}
			aHelper[i] = new AnimationHelper(frameDuration, frames, idle[i],offsetX, offsetY);
		}
		aMap.put(fileName, aHelper);
		this.direction = direction.getVal();
	}
	
	public AnimationHelper getAnimMap() {
		return aMap.get(currentAnimation)[direction];
	}
	
	public TextureRegion getIdle() {
		return aMap.get(currentAnimation)[direction].idleFrame;
	}
	
	public float getStateTime() {
		return aMap.get(currentAnimation)[direction].stateTime;
	}
	
	public void setStateTime(float time) {
		aMap.get(currentAnimation)[direction].stateTime = time;
	}
	
	public void addToStateTime(float time) {
		aMap.get(currentAnimation)[direction].stateTime+=time;
	}
	public void changeAnimation(String nAnimation) {
		if(aMap.containsKey(nAnimation)) {
			prevAnimation = currentAnimation;
			currentAnimation = nAnimation;
			aMap.get(prevAnimation)[direction].stateTime=0;
		}
	}
	public void changeDirection(DirectionType direction) {
		aMap.get(currentAnimation)[this.direction].stateTime = 0;
		this.direction = direction.getVal();
	}
	public void goToPrevAnimation() {
		String tmp = currentAnimation;
		currentAnimation = prevAnimation;
		prevAnimation = tmp;
		aMap.get(currentAnimation)[this.direction].stateTime = 0;
	}
}
