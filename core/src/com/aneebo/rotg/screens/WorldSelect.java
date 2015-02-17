package com.aneebo.rotg.screens;

import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class WorldSelect implements Screen {

	private OrthogonalTiledMapRenderer renderer;
	private TiledMap tiledMap;
	private OrthographicCamera cam;
	private FitViewport vPort;
	private ShapeRenderer sRender;
	
	@Override
	public void show() {
		tiledMap = new TmxMapLoader().load(Constants.WORLD_MAP);
		renderer = new OrthogonalTiledMapRenderer(tiledMap);
		sRender = new ShapeRenderer();
		cam = new OrthographicCamera();
		vPort = new FitViewport(Constants.WIDTH, Constants.HEIGHT, cam);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		checkMapClicks();
		
		sRender.setProjectionMatrix(cam.combined);
		renderer.setView(cam);
		renderer.render();
		drawCities();
		
	}
	
	private static final int RADIUS = 46;
	private float lastX;
	private float lastY;

	private void checkMapClicks() {
		MapLayer layer = tiledMap.getLayers().get("cities");
		MapObjects cities = layer.getObjects();
		
		float touchX = Gdx.input.getX();
		float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		if(touchX==lastX && touchY==lastY) return;
		
		for(MapObject city : cities) {
			Float x = (Float)city.getProperties().get("x");
			Float y = (Float)city.getProperties().get("y");
			if(touchX >= (x - RADIUS) && touchX <= (x + RADIUS) &&
					touchY >= (y - RADIUS) && touchY <= (y + RADIUS)) {
				System.out.println(city.getName());
			}
		}
		lastX = touchX;
		lastY = touchY;
	}
	
	private void drawCities() {
		MapLayer layer = tiledMap.getLayers().get("cities");
		MapObjects cities = layer.getObjects();
		sRender.begin(ShapeType.Line);
		for(MapObject city : cities) {
			Float x = (Float)city.getProperties().get("x");
			Float y = (Float)city.getProperties().get("y");
			sRender.circle(x.floatValue(), y.floatValue(), RADIUS);
		}	
		sRender.end();
	}

	@Override
	public void resize(int width, int height) {
		vPort.update(width, height, true);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		renderer.dispose();
		sRender.dispose();
	}

}
