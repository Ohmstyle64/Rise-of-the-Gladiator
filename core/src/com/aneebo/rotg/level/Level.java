package com.aneebo.rotg.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.Array;

public abstract class Level implements EntityListener{
	
	public int totalRounds;
	public int currentRound;
	public Array<Round> rounds;
	public String mapLocation;
	public Prize prize;
	protected Engine engine;
	protected Entity player;
	
	
	public Level(Engine engine, Entity player) {
		this.engine = engine;
		this.player = player;
		engine.addEntityListener(this);
		currentRound = 0;
		rounds = new Array<Round>();
		loadLevel();
	}
	
	public boolean isCompleted() {
		return currentRound >= totalRounds;
	}
	
	public class Round {
		public Array<Entity> enemies;
		
		public Round(Array<Entity> enemies) {
			this.enemies = enemies;
		}
	}
	
	public void addEntities() {
		Round r = rounds.get(currentRound);
		for(Entity e : r.enemies) {
			engine.addEntity(e);
		}
	}
	
	protected abstract void loadLevel();
	
}
