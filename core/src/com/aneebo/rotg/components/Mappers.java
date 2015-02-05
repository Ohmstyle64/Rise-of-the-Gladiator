package com.aneebo.rotg.components;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {
	public static final ComponentMapper<AbilityComponent> abMap = ComponentMapper.getFor(AbilityComponent.class);
	public static final ComponentMapper<AIComponent> aiMap = ComponentMapper.getFor(AIComponent.class);
	public static final ComponentMapper<CollisionComponent> colMap = ComponentMapper.getFor(CollisionComponent.class);
	public static final ComponentMapper<InputComponent> inpMap = ComponentMapper.getFor(InputComponent.class);
	public static final ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<RenderComponent> renMap = ComponentMapper.getFor(RenderComponent.class);
	public static final ComponentMapper<StatComponent> staMap = ComponentMapper.getFor(StatComponent.class);
	public static final ComponentMapper<ProjectileComponent> projMap = ComponentMapper.getFor(ProjectileComponent.class);
	public static final ComponentMapper<InventoryComponent> invMap = ComponentMapper.getFor(InventoryComponent.class);
}
