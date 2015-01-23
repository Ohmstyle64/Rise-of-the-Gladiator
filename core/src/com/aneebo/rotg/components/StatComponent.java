package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class StatComponent extends Component {
	public String name;
	public float health;
	public float max_health;
	public float energy;
	public float max_energy;
	public float health_regen;
	public float energy_regen;
	public float eValue;
	public Color color;
	public float speed;
	
	public StatComponent(String name, float health, float energy, Color color, float health_regen, float energy_regen, float speed) {
		this.name = name;
		this.health = health;
		this.energy = energy;
		this.max_energy = energy;
		this.max_health = health;
		this.color = color;
		this.health_regen = health_regen;
		this.energy_regen = energy_regen;
		this.speed = speed;
		this.eValue = 0;
	}
}
