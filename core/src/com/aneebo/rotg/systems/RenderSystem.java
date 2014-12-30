package com.aneebo.rotg.systems;

import com.aneebo.rotg.components.MovementComponent;
import com.aneebo.rotg.components.RenderComponent;
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

	private static final float TILE_WIDTH = 32f;
	private static final float TILE_HEIGHT = 32f;
	
	private OrthographicCamera arenaCam;
	private OrthogonalTiledMapRenderer renderer;
	private BitmapFont font;
	
	private ImmutableArray<Entity> entities;
	private ComponentMapper<MovementComponent> mc = ComponentMapper.getFor(MovementComponent.class);
	private ComponentMapper<RenderComponent> rc = ComponentMapper.getFor(RenderComponent.class);
	
	private MovementComponent move;
	private RenderComponent render;
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
			move = mc.get(e);
			render = rc.get(e);
			renderer.getBatch().draw(render.texture, move.curXPos*TILE_WIDTH, move.curYPos*TILE_HEIGHT);
		}
		
		//DEV INFO
		font.draw(renderer.getBatch(), "Mouse Pos (X,Y): ("+Gdx.input.getX()+", "+(Gdx.graphics.getHeight()-Gdx.input.getY())+")"
				+", ("+(int)(Gdx.input.getX()/TILE_WIDTH)+", "+(int)((Gdx.graphics.getHeight()-Gdx.input.getY())/TILE_HEIGHT)+")", 10, 20);
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
