package com.aneebo.rotg.ui;

import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class HealthBlocks implements Disposable {
	
	private float x, y, min, max, curValue;
	private TextureRegion[] blocks;
	
	public HealthBlocks(float min, float max, float curValue) {
		this.min = min;
		this.max = max;
		this.curValue = curValue;
		TextureAtlas atlas = Assets.assetManager.get(Constants.HEALTH_ATLAS, TextureAtlas.class);
		blocks = new TextureRegion[8];
		for(int i = 0; i < 8; i++) {
			blocks[i] = atlas.findRegion("healthBlock"+i);
		}
		x = 0;
		y = 0;
	}
	
	public void setValue(float value) {
		MathUtils.clamp(curValue, min, max);
		this.curValue = value;
	}
	
	public void draw(Batch batch) {
		int index = 9 - (int) (curValue / max * 100f / 12.5f + 1);
		if(index < 8) {
			batch.draw(blocks[index], x, y);
		}
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void dispose() {
		for(TextureRegion region : blocks) {
			region.getTexture().dispose();
		}
	}
	
	
}
