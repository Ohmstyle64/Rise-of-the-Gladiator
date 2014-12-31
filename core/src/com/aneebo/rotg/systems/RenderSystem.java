package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.AbilityComponent;
import com.aneebo.rotg.components.PositionComponent;
import com.aneebo.rotg.components.RenderComponent;
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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class RenderSystem extends EntitySystem {

	private OrthographicCamera arenaCam;
	private OrthogonalTiledMapRenderer renderer;
	private BitmapFont font;
	
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> abilityEntities;
	
	private ComponentMapper<PositionComponent> pc = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<RenderComponent> rc = ComponentMapper.getFor(RenderComponent.class);
	private ComponentMapper<AbilityComponent> ac = ComponentMapper.getFor(AbilityComponent.class);
	
	private AbilityComponent abilityComponent;
	private PositionComponent posComponent;
	private RenderComponent renderComponent;
	private Entity e;
	
	public RenderSystem(OrthogonalTiledMapRenderer renderer) {
		super(3);
		this.renderer = renderer;
		font = new BitmapFont();
		arenaCam = new OrthographicCamera();
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(RenderComponent.class));
		abilityEntities = engine.getEntitiesFor(Family.getFor(AbilityComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//RENDER MAP
		renderer.setView(arenaCam);
		renderer.render();
		
		//RENDER CHARACTERS
		renderer.getBatch().begin();
		int size = entities.size();
		for(int i = 0; i < size; i++) {
			e = entities.get(i);
			posComponent = pc.get(e);
			renderComponent = rc.get(e);
			renderer.getBatch().draw(renderComponent.texture, posComponent.curXPos*Constants.TILE_WIDTH, 
					posComponent.curYPos*Constants.TILE_HEIGHT);
		}
		
		//RENDER ABILITIES
		size = abilityEntities.size();
		for(int i = 0; i < size; i++) {
			e = abilityEntities.get(i);
			abilityComponent = ac.get(e);
			if(abilityComponent.ability==null) continue;
			abilityComponent.ability.render(renderer.getBatch());
		}
		
		//DEV INFO
		font.draw(renderer.getBatch(), "Mouse Pos (X,Y): ("+Gdx.input.getX()+", "+(Gdx.graphics.getHeight()-Gdx.input.getY())+")"
				+", ("+(int)(Gdx.input.getX()/Constants.TILE_WIDTH)+", "+
				(int)((Gdx.graphics.getHeight()-Gdx.input.getY())/Constants.TILE_HEIGHT)+")", 10, 20);
		font.draw(renderer.getBatch(), "FPS: "+Gdx.graphics.getFramesPerSecond() , 350, 20);
		renderer.getBatch().end();
	}
	

	public OrthographicCamera getArenaCam() {
		return arenaCam;
	}

	public void dispose() {
		renderer.dispose();
		font.dispose();
	}
}
