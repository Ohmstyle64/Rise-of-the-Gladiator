package com.aneebo.rotg.components;

import com.aneebo.rotg.ui.HealthBlocks;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class StatComponent extends Component {
	public String name;
	public String gender;
	public float health;
	public float max_health;
	public float energy;
	public float max_energy;
	public float health_regen;
	public float energy_regen;
	public float eValue;
	public Color color;
	public float speed;
	public int skillPoints;
	public int currency;
	
	public boolean isPlayer;
	
	public transient HealthBlocks healthBlocks;
	
	public float increaseToAttackSpeed;
	public float increaseToDamage;
	public float increaseToRange;
	public float damageMitigation;
	public float magicResist;
	
	public StatComponent() {
		
	}
	
	public StatComponent(String name, String gender, float health, float energy, Color color, float health_regen, float energy_regen, float speed, boolean isPlayer) {
		this.name = name;
		this.gender = gender;
		this.health = health;
		this.energy = energy;
		this.max_energy = energy;
		this.max_health = health;
		this.color = color;
		this.health_regen = health_regen;
		this.energy_regen = energy_regen;
		this.speed = speed;
		this.eValue = 0;
		this.isPlayer = isPlayer;
		healthBlocks = new HealthBlocks(0, max_health, max_health);
		skillPoints = 0;
		currency = 0;
		increaseToAttackSpeed = 0;
		increaseToDamage = 0;
		increaseToRange = 0;
		damageMitigation = 0;
		magicResist = 0;
	}	
}
