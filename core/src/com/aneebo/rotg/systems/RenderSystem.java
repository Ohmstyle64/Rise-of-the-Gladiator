package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.InputComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
import com.aneebo.rotg.components.StatComponent;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class RenderSystem extends EntitySystem {

	private OrthographicCamera arenaCam;
	private OrthogonalTiledMapRenderer renderer;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> abilityEntities;
	private Entity player;
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<RenderComponent> rc = ComponentMapper.getFor(RenderComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	private ComponentMapper<StatComponent> sc = ComponentMapper.getFor(StatComponent.class);
	
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private RenderComponent renderComponent;
	private StatComponent statComponent;
	private Entity e;
	
	public RenderSystem(OrthogonalTiledMapRenderer renderer) {
		super(4);
		this.renderer = renderer;
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		arenaCam = new OrthographicCamera();
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(RenderComponent.class));
		abilityEntities = engine.getEntitiesFor(Family.getFor(AbilityComponent.class));
		player = engine.getEntitiesFor(Family.getFor(InputComponent.class)).first();
	}
	
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//RENDER MAP
		renderer.setView(arenaCam);
		renderer.render();
		
		//RENDER CHARACTERS
		shapeRenderer.begin(ShapeType.Line);
		renderer.getBatch().begin();
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			posComponent = pc.get(e);
			renderComponent = rc.get(e);
			statComponent = sc.get(e);
			renderer.getBatch().draw(renderComponent.texture,
					posComponent.curXPos*Constants.TILE_WIDTH, 
					posComponent.curYPos*Constants.TILE_HEIGHT);
			font.draw(renderer.getBatch(), 
					statComponent.name,
					posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT + 70);
			font.draw(renderer.getBatch(), statComponent.health+"",
					posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT + 50);
			shapeRenderer.setColor(statComponent.color);
			shapeRenderer.rect(posComponent.curXPos*Constants.TILE_WIDTH,
					posComponent.curYPos*Constants.TILE_HEIGHT, 
					Constants.TILE_WIDTH, 
					Constants.TILE_HEIGHT);
		}
		
		//RENDER ABILITIES
		size = abilityEntities.size();
		for(int i = 0; i < size; i++) {
			e = abilityEntities.get(i);
			abilityComponent = ac.get(e);
			int abilSize = abilityComponent.abilitySlots.size;
			for(int j = 0; j < abilSize; j++) {
				//TEMP CODE FOR DEV
				if(abilityComponent.abilitySlots.get(j).isActivated && abilityComponent.abilitySlots.get(j).isAvailable) {
					posComponent = pc.get(e);
					statComponent = sc.get(e);
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
				(int)((Gdx.graphics.getHeight()-Gdx.input.getY())/Constants.TILE_HEIGHT)+")", 450, 20);
		font.draw(renderer.getBatch(), "FPS: "+Gdx.graphics.getFramesPerSecond() , 350, 20);
		
		renderer.getBatch().end();
		shapeRenderer.end();
		

	}
	

	public OrthographicCamera getArenaCam() {
		return arenaCam;
	}

	public void dispose() {
		renderer.dispose();
		font.dispose();
	}
}
