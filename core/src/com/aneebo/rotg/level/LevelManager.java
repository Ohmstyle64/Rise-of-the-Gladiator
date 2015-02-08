package com.aneebo.rotg.level;

import com.aneebo.rotg.screens.Play;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelManager {
	private ObjectMap<Integer, Level> levelList;
	private Level currentLevel;
	private int nextLevel;
	private boolean isTransitionIn;
	private boolean isTransitionOut;
	
	private OrthogonalTiledMapRenderer renderer;
	
	public LevelManager(ObjectMap<Integer, Level> levels, int startLevel) {
		levelList = levels;
		currentLevel = levelList.get(startLevel);
		renderer = new OrthogonalTiledMapRenderer(currentLevel.tiledMap);
		currentLevel.loadLevel();
		currentLevel.addEntities();
		isTransitionIn = false;
		isTransitionOut = false;
	}
	
	public void goToLevel(Integer levelId) {
		nextLevel = levelId;
		isTransitionOut = true;
	}
	
	public void transitionOut(boolean val) {
		isTransitionOut = val;
	}
	
	public void transitionIn(boolean val) {
		isTransitionIn = val;
	}
	
	public void update(float deltaTime) {
		if(isTransitionIn) {
			currentLevel.transitionIn(deltaTime);
		}else if(isTransitionOut) {
			currentLevel.transitionOut(deltaTime);
		}
	}
	
	private void changeMap() {
		currentLevel = levelList.get(nextLevel);
		renderer.setMap(currentLevel.tiledMap);
		isTransitionIn = true;
	}
	
	public void enterLevel() {
		currentLevel.loadLevel();
		currentLevel.addEntities();
		Play.levelManager.transitionIn(false);

	}
	
	public void leaveLevel() {
		currentLevel.removeEntities();
		changeMap();
		transitionOut(false);
	}
	
	public OrthogonalTiledMapRenderer renderer() {
		return renderer;
	}
	
}
