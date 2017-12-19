package com.vekteur.chess;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Logger;

public class MainGame extends Game {

  	public static SpriteBatch spriteBatch;
	public static ShapeRenderer shapeRenderer;
	public static BitmapFont font;
	public static Logger logger;
	public static Skin skin;

	public void create() {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		logger = new Logger("Chess");
		logger.setLevel(Logger.DEBUG);
		
		FreeTypeFontGenerator arialGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/Fonts/arial.ttf"));
		FreeTypeFontParameter typeParameter = new FreeTypeFontParameter();
		typeParameter.size = 40;
		BitmapFont font40 = arialGenerator.generateFont(typeParameter);
		typeParameter.size = 80;
		BitmapFont font80 = arialGenerator.generateFont(typeParameter);
		arialGenerator.dispose();
		
		skin = new Skin();
		skin.add("arial-font", font40);
		skin.add("big-arial-font", font80);
		skin.addRegions(new TextureAtlas(Gdx.files.internal("assets/Skin/uiskin.atlas")));
		skin.load(Gdx.files.internal("assets/Skin/uiskin.json"));
		
		new Menu(this);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		font.dispose();
		skin.dispose();
	}
}
