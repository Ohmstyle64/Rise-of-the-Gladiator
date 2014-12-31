package com.aneebo.rotg.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class GuiComponent extends Component {
	public Widget widget;
	
	public GuiComponent(Widget widget) {
		this.widget = widget;
	}
}
