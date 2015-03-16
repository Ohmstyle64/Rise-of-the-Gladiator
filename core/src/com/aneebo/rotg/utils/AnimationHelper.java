

package com.aneebo.rotg.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationHelper extends Animation {

	public TextureRegion idleFrame;
	public float stateTime, offsetX, offsetY;
	
	public AnimationHelper(float frameDuration,
			Array<? extends TextureRegion> keyFrames, TextureRegion idleFrame, float offsetX, float offsetY) {
		super(frameDuration, keyFrames);
		this.idleFrame = idleFrame;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		stateTime = 0;
	}

}
