package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationComponent extends Component {
	public TextureRegion[] idle;
	public Animation[] animations;
	public float stateTime;
	
	public AnimationComponent(Texture anim_Texture, int anim_Width, int anim_Height, float frameDuration) {
		anim_Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tr = new TextureRegion(anim_Texture);
		TextureRegion[][] textures = tr.split(anim_Width, anim_Height);
		animations = new Animation[4];
		idle = new TextureRegion[4];
		for(int i = 0; i < textures.length; i++) {
			Array<TextureRegion> frames = new Array<TextureRegion>(textures[0].length - 1);
			for(int j = 0; j < textures[0].length; j++) {
				if(j == 0 ) {
					idle[i] = textures[i][j];
				}else {
					frames.add(textures[i][j]);
				}
			}
			animations[i] = new Animation(frameDuration,frames);
		}
		stateTime = 0;
	}
}
