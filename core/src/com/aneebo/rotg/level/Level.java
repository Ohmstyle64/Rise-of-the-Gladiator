package com.aneebo.rotg.level;

import com.aneebo.rotg.inventory.Item;
import com.aneebo.rotg.inventory.items.EmptyItem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Level{
	
	public int totalRounds;
	public int currentRound;
	public Array<Round> rounds;
	public String mapLocation;
	public Prize prize;
	protected Engine engine;
	protected Entity player;
	protected TiledMap tiledMap;
	protected Vector2 playerStart;
	protected LevelManager manager;
	
	public Level(Engine engine, Entity player, String mapLocation, Vector2 playerStart, LevelManager manager) {
		this.engine = engine;
		this.player = player;
		this.mapLocation = mapLocation;
		this.playerStart = playerStart;

		currentRound = 0;
		tiledMap = new TmxMapLoader().load(mapLocation);
		
		rounds = new Array<Round>();
		
		this.manager = manager;
	}
	
	public boolean isCompleted() {
		if(currentRound >= totalRounds) {
			currentRound = totalRounds - 1;
			return true;
		}
		return false;
	}
	
	public class Round {
		public Array<Entity> entities;
		
		public Round(Array<Entity> entities) {
			this.entities = entities;
		}
	}
	
	public void addEntities() {
		Round r = rounds.get(currentRound);
		for(Entity e : r.entities) {
			engine.addEntity(e);
		}
	}
	
	public void removeEntities() {
		Round r = rounds.get(currentRound);
		for(Entity e : r.entities) {
			engine.removeEntity(e);
		}
	}
	
	protected Array<Item> fillRest(Array<Item> array) {
		int size = array.items.length;
		for(int i = array.size; i < size; i++) {
			array.add(new EmptyItem());
		}
		return array;
	}
	
	protected abstract void loadLevel();

	public abstract void transitionIn(float deltaTime);

	public abstract void transitionOut(float deltaTime);	
}
