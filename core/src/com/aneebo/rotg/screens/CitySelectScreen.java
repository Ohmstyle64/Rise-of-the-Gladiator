package com.aneebo.rotg.screens;

import com.aneebo.rotg.utils.Constants;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CitySelectScreen implements Screen {

	private OrthogonalTiledMapRenderer renderer;
	private TiledMap tiledMap;
	private OrthographicCamera cam;
	private FitViewport vPort;
	private ShapeRenderer sRender;
	private String jsonData;
	
	private static final int RADIUS = 46;
	private Circle[] cityArea;
	private MapObjects cities;
		
		public CitySelectScreen(String jsonData) {
			this.jsonData = jsonData; 
		}
	
		@Override
		public void show() {
		tiledMap = new TmxMapLoader().load(Constants.WORLD_MAP);
		renderer = new OrthogonalTiledMapRenderer(tiledMap);
		sRender = new ShapeRenderer();
		cam = new OrthographicCamera();
		vPort = new FitViewport(Constants.WIDTH, Constants.HEIGHT, cam);
		vPort.apply(true);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		MapLayer layer = tiledMap.getLayers().get("cities");
		cities = layer.getObjects();
		cityArea = new Circle[cities.getCount()];
		
		for(int i = 0; i < cityArea.length; i++) {
			MapObject city = cities.get(i);
			Float x = (Float)city.getProperties().get("x");
			Float y = (Float)city.getProperties().get("y");
			Vector3 screenCoor = cam.project(new Vector3(x,y,0));
			cityArea[i] = new Circle(screenCoor.x, screenCoor.y, RADIUS);
		}
		
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
	


	private void checkMapClicks() {
		
		float touchX = Gdx.input.getX();
		float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
		for(int i = 0; i < cityArea.length; i++) {
			Circle c = cityArea[i];
			if(c.contains(touchX, touchY) && Gdx.input.isTouched()) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new CityScreen(cities.get(i).getName(),jsonData));
			}
		}
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
		dispose();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		sRender.dispose();
	}

}
