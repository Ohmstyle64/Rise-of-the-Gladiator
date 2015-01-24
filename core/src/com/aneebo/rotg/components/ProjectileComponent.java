package com.aneebo.rotg.components;

import com.aneebo.rotg.abilities.RangeAbility;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjectileComponent extends Component {
	public Entity from;
	public RangeAbility rangeAbility;
	public Array<Vector2> path;
	public Entity hit;
	public boolean hitInAnimmate;
	
	public ProjectileComponent(Entity from, RangeAbility rangeAbility, Array<Vector2> path) {
		this.from = from;
		this.rangeAbility = rangeAbility;
		this.path = path;
		this.hitInAnimmate = false;
	}
	
	public boolean isCollided() {
		return hit != null;
	}
}
