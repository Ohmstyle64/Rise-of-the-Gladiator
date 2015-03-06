package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.MerchantComponent;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

public class MerchantSystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	private MerchantComponent merchantComponent;
	private Entity e;
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
			entities = engine.getEntitiesFor(Family.all(MerchantComponent.class).get());
	}
	
	@Override
	public void update(float deltaTime) {
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			merchantComponent = Mappers.mercMap.get(e);
			if(merchantComponent.isInitiated) {
				if(!merchantComponent.isSelling) {
					Gdx.app.log("Dialogue", merchantComponent.storedDialogue[merchantComponent.dialogPart]);
					merchantComponent.dialogPart++;
					if(merchantComponent.dialogPart >= 2) {
						merchantComponent.dialogPart = 0;
						merchantComponent.isInitiated = false;
					}else {
						merchantComponent.isSelling = true;
					}
				}
			}
		}
	}
}
