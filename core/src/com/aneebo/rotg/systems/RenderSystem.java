package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.AnimationComponent;
import com.aneebo.rotg.components.Mappers;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class RenderSystem extends EntitySystem {

	private OrthographicCamera arenaCam;
	private FitViewport arenaViewPort;
	private OrthogonalTiledMapRenderer renderer;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> abilityEntities;
	private ImmutableArray<Entity> animEntities;
	
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private RenderComponent renderComponent;
	private StatComponent statComponent;
	private AnimationComponent animComponent;
	private Animation anim;
	private Entity e;
	
	
	public RenderSystem(OrthogonalTiledMapRenderer renderer) {
		super(4);
		this.renderer = renderer;
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		arenaCam = new OrthographicCamera();
		arenaViewPort = new FitViewport(Constants.WIDTH, Constants.HEIGHT, arenaCam);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(RenderComponent.class));
		animEntities = engine.getEntitiesFor(Family.getFor(AnimationComponent.class));
		abilityEntities = engine.getEntitiesFor(Family.getFor(AbilityComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
			statComponent = Mappers.staMap.get(e);
			renderer.getBatch().draw(Assets.assetManager.get(renderComponent.textureName, Texture.class),
					posComponent.curXPos*Constants.TILE_WIDTH, 
					posComponent.curYPos*Constants.TILE_HEIGHT);
			if(statComponent== null) continue;
			font.draw(renderer.getBatch(), 
					statComponent.name,
					posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT + 70);
			font.draw(renderer.getBatch(), String.format("%.1f", statComponent.health)+"",
					posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT + 50);
			shapeRenderer.setColor(statComponent.color);
			shapeRenderer.rect(posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT, 
					Constants.TILE_WIDTH, 
					Constants.TILE_HEIGHT);
		}
		
		//RENDER ANIMATIONS
		size = animEntities.size();
		for(int i = 0; i < size; i++) {
			e = animEntities.get(i);
			posComponent = Mappers.posMap.get(e);
			animComponent = Mappers.animMap.get(e);
			statComponent = Mappers.staMap.get(e);
			
			if(posComponent.isStopped()) {
				renderer.getBatch().draw(animComponent.idle[posComponent.direction.getVal()],
						posComponent.curXPos * Constants.TILE_WIDTH,
						posComponent.curYPos * Constants.TILE_HEIGHT);
				animComponent.stateTime = 0;
			}else {
				animComponent.stateTime += deltaTime;
				anim = animComponent.animations[posComponent.direction.getVal()];
				renderer.getBatch().draw(anim.getKeyFrame(animComponent.stateTime),
						posComponent.curXPos * Constants.TILE_WIDTH,
						posComponent.curYPos * Constants.TILE_HEIGHT);
				if(anim.isAnimationFinished(animComponent.stateTime)) {
					animComponent.stateTime = 0;
				}
			}
			
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
	}
}
