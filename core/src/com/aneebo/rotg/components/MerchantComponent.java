package com.aneebo.rotg.components;

import com.aneebo.rotg.inventory.Inventory;
import com.aneebo.rotg.ui.MerchantInventoryWindow;
import com.aneebo.rotg.utils.Assets;
import com.aneebo.rotg.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MerchantComponent extends Component {
	public boolean isInitiated;
	public boolean isSelling;
	public Inventory inventory;
	public MerchantInventoryWindow window;
	public String[] storedDialogue;
	public int dialogPart;
	
	public MerchantComponent(Inventory inventory, String[] storedDialogue) {
		this.inventory = inventory;
		this.storedDialogue = storedDialogue;
		isInitiated = false;
		isSelling = false;
		dialogPart = 0;
		window = new MerchantInventoryWindow("Trader", Assets.assetManager.get(Constants.UI_SKIN, Skin.class), this);
		window.setVisible(false);
	}
}
