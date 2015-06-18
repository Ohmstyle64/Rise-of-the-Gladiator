package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.AnimationComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.MerchantComponent;
import com.aneebo.rotg.components.ParticleComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.ui.HealthBlocks;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.aneebo.rotg.utils.RoTGCamera;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class RenderSystem extends EntitySystem {

	private RoTGCamera arenaCam;
	private FitViewport arenaViewPort;
	private OrthogonalTiledMapRenderer renderer;
	private ShapeRenderer shapeRenderer;
	private Stage stage;
	private BitmapFont font;
	
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> abilityEntities;
	private ImmutableArray<Entity> animEntities;
	private ImmutableArray<Entity> particleEntities;
	private ImmutableArray<Entity> merchantEntities;
	private ImmutableArray<Entity> statEntities;
	
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private RenderComponent renderComponent;
	private StatComponent statComponent;
	private AnimationComponent animComponent;
	private ParticleComponent partComponent;
	private MerchantComponent merchantComponent;
	private Animation anim;
	private Entity e;
	
	public RenderSystem(OrthogonalTiledMapRenderer renderer, Stage stage, RoTGCamera camera) {
		super(4);
		this.renderer = renderer;
		font = Assets.assetManager.get(Constants.FONT, BitmapFont.class);
		shapeRenderer = new ShapeRenderer();
		this.stage = stage;
		arenaCam = camera;
		arenaViewPort = new FitViewport(Constants.WIDTH, Constants.HEIGHT, arenaCam);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(RenderComponent.class).get());
		animEntities = engine.getEntitiesFor(Family.all(AnimationComponent.class).get());
		abilityEntities = engine.getEntitiesFor(Family.all(AbilityComponent.class).get());
		particleEntities = engine.getEntitiesFor(Family.all(ParticleComponent.class).get());
		merchantEntities = engine.getEntitiesFor(Family.all(MerchantComponent.class).get());
		statEntities = engine.getEntitiesFor(Family.all(StatComponent.class).get());
		
		int size = merchantEntities.size();
		for(int i = 0; i < size; i++) {
			e = merchantEntities.get(i);
			merchantComponent = Mappers.mercMap.get(e);
			stage.addActor(merchantComponent.window);
		}
		
		size = statEntities.size();
		for(int i = 0; i < size; i++) {
			e = statEntities.get(i);
			statComponent = Mappers.staMap.get(e);
			statComponent.healthBlocks = new HealthBlocks(0, statComponent.max_health, statComponent.max_health);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(arenaCam.time > 0) {
			arenaCam.update(deltaTime);
		}
		//RENDER MAP
		renderer.setView(arenaCam);
		renderer.render();
		
		shapeRenderer.setProjectionMatrix(arenaCam.combined);
		
		//RENDER STATIC CHARACTERS
		shapeRenderer.begin(ShapeType.Line);
		renderer.getBatch().begin();
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			posComponent = Mappers.posMap.get(e);
			renderComponent = Mappers.renMap.get(e);
			if(renderComponent.shake) {
				arenaCam.smallShake();
				renderComponent.shake = false;
			}
			//Flashing Red Effect
			float savedRed = 0, savedGreen = 0, savedBlue = 0;
			if(renderComponent.frame_limit != 0 && renderComponent.frames < renderComponent.frame_limit) {
				renderComponent.frames++;
				Color c = renderer.getBatch().getColor();
				savedRed = c.r;
				savedGreen = c.g;
				savedBlue = c.b;
				renderer.getBatch().setColor(1, 0, 0, 1);
			}
			renderer.getBatch().draw(Assets.assetManager.get(renderComponent.textureName, Texture.class),
					posComponent.curXPos*Constants.TILE_WIDTH, 
					posComponent.curYPos*Constants.TILE_HEIGHT);
			
			//End Flashing Red
			if(renderComponent.frame_limit != 0) {
				renderer.getBatch().setColor(savedRed, savedGreen, savedBlue, 1);
				if(renderComponent.frames >= renderComponent.frame_limit) {
					renderComponent.reset();
				}
			}
			statComponent = Mappers.staMap.get(e);
			if(statComponent == null) continue;
			statComponent.healthBlocks.setPosition(posComponent.curXPos*Constants.TILE_WIDTH, (posComponent.curYPos-1f)*Constants.TILE_HEIGHT);
			statComponent.healthBlocks.setValue(statComponent.health);
			statComponent.healthBlocks.draw(renderer.getBatch());
		}
		
		//RENDER PARTICLES
		size = particleEntities.size();
		for(int i = 0; i < size; i++) {
			e = particleEntities.get(i);
			partComponent = Mappers.partMap.get(e);
			partComponent.pEffect.draw(renderer.getBatch(), deltaTime);
		}
		
		//RENDER INVENTORY WINDOWS
		size = merchantEntities.size();
		for(int i = 0; i < size; i++) {
			e = merchantEntities.get(i);
			merchantComponent = Mappers.mercMap.get(e);
			if(merchantComponent.isSelling) {
				merchantComponent.window.setVisible(true);
			}
		}
		
		//RENDER ANIMATIONS
		size = animEntities.size();
		for(int i = 0; i < size; i++) {
			e = animEntities.get(i);
			posComponent = Mappers.posMap.get(e);
			animComponent = Mappers.animMap.get(e);
			statComponent = Mappers.staMap.get(e);
			
//			if(posComponent.isStopped()) {
//				renderer.getBatch().draw(animComponent.idle[posComponent.direction.getVal()],
//						posComponent.curXPos * Constants.TILE_WIDTH,
//						posComponent.curYPos * Constants.TILE_HEIGHT);
//				animComponent.stateTime = 0;
//			}else {
//				animComponent.stateTime += deltaTime;
//				anim = animComponent.animations[posComponent.direction.getVal()];
//				renderer.getBatch().draw(anim.getKeyFrame(animComponent.stateTime),
//						posComponent.curXPos * Constants.TILE_WIDTH,
//						posComponent.curYPos * Constants.TILE_HEIGHT);
//				if(anim.isAnimationFinished(animComponent.stateTime)) {
//					animComponent.stateTime = 0;
//				}
//			}
		}
		
		//RENDER ABILITIES
		size = abilityEntities.size();
		for(int i = 0; i < size; i++) {
			e = abilityEntities.get(i);
			abilityComponent = Mappers.abMap.get(e);
			int abilSize = abilityComponent.abilitySlots.size;
			for(int j = 0; j < abilSize; j++) {
				//TEMP CODE FOR DEV
				if(abilityComponent.abilitySlots.get(j).isActivated && abilityComponent.abilitySlots.get(j).isAvailable) {
					posComponent = Mappers.posMap.get(e);
					statComponent = Mappers.staMap.get(e);
					shapeRenderer.setColor(statComponent.color);
					shapeRenderer.circle(posComponent.curXPos*Constants.TILE_WIDTH+Constants.TILE_WIDTH / 2, 
							posComponent.curYPos*Constants.TILE_HEIGHT+Constants.TILE_HEIGHT / 2, 
							abilityComponent.abilitySlots.get(j).getRange()*Constants.TILE_WIDTH);
					abilityComponent.abilitySlots.get(j).render(renderer.getBatch());
				}
				abilityComponent.ftm.update(deltaTime);
				abilityComponent.ftm.draw(renderer.getBatch(), font);
			}
		}
		
		//DEV INFO
		font.draw(renderer.getBatch(), "Mouse Pos (X,Y): ("+Gdx.input.getX()+", "+(Gdx.graphics.getHeight()-Gdx.input.getY())+")"
				+", ("+(int)(Gdx.input.getX()/Constants.TILE_WIDTH)+", "+
				(int)((Gdx.graphics.getHeight()-Gdx.input.getY())/Constants.TILE_HEIGHT)+")", 250, 20);
		font.draw(renderer.getBatch(), "FPS: "+Gdx.graphics.getFramesPerSecond() , 150, 20);
		
		renderer.getBatch().end();
		shapeRenderer.end();
		
	}
	

	public FitViewport getViewport() {
		return arenaViewPort;
	}

	public void dispose() {
		renderer.dispose();
		font.dispose();
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			statComponent = Mappers.staMap.get(e);
			if(statComponent == null) continue;
			statComponent.healthBlocks.dispose();
		}
	}
}
