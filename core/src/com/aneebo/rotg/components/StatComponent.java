package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class StatComponent extends Component {
	public String name;
	public float health;
	public float max_health;
	public float energy;
	public float max_energy;
	public float eValue;
	public Color color;
	
	public StatComponent(String name, float health, float energy, Color color) {
		this.name = name;
		this.health = health;
		this.energy = energy;
		this.max_energy = energy;
		this.max_health = health;
		this.color = color;
	}
}
