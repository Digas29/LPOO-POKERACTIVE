package com.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AssetManager {
	private SpriteBatch batch;
    private BitmapFont font;
    private static AssetManager manager = null;
    
    protected AssetManager(){
        batch = new SpriteBatch();    
        font = new BitmapFont();
    }
    
    public static AssetManager getAssetManager(){
    	if(manager == null)
    		manager = new AssetManager();
    	return manager;
    }
    
	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}
}
