package com.aneebo.rotg.level;

import com.aneebo.rotg.types.LevelType;
import com.aneebo.rotg.ui.FloatingTextManager;
import com.aneebo.rotg.utils.RoTGCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelManager {
	private ObjectMap<LevelType, Level> levelList;
	private Level currentLevel;
	private LevelType nextLevel;
	private boolean isTransitionIn;
	private boolean isTransitionOut;
	private FloatingTextManager ftm;
	private RoTGCamera camera;
	
	private OrthogonalTiledMapRenderer renderer;
	
	public LevelManager(FloatingTextManager ftm, RoTGCamera camera) {
		this.ftm = ftm;
		this.camera = camera;
	}
	
	public void createLevels(ObjectMap<LevelType, Level> levelList, LevelType startLevel) {
		this.levelList = levelList;
		currentLevel = levelList.get(startLevel);
		renderer = new OrthogonalTiledMapRenderer(currentLevel.tiledMap);
		currentLevel.loadLevel();
		currentLevel.addEntities();
		isTransitionIn = false;
		isTransitionOut = false;
	}
	
	public void goToLevel(LevelType levelId) {
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
		transitionIn(false);

	}
	
	public void leaveLevel() {
		currentLevel.removeEntities();
		changeMap();
		transitionOut(false);
	}
	
	public OrthogonalTiledMapRenderer renderer() {
		return renderer;
	}

	public FloatingTextManager getFloatingTextMgr() {
		return ftm;
	}

	public RoTGCamera getCamera() {
		return camera;
	}
	
	
	
}
