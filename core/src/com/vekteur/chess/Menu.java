package com.vekteur.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vekteur.chess.input.MouseInput.Players;

public class Menu implements Screen {
	private MainGame mainGame;
	private Stage stage;
	
	public Menu(MainGame _mainGame)
	{
		mainGame = _mainGame;
		

		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		VerticalGroup table = new VerticalGroup();
		stage.addActor(table);
		table.setFillParent(true);
		
		Label title = new Label("Chess", MainGame.skin, "arial");
		table.addActor(title);
		
		TextButton button1 = new TextButton("Player VS Player", MainGame.skin, "arial");
		table.addActor(button1);
		
		TextButton button2 = new TextButton("Player VS AI", MainGame.skin, "arial");
		table.addActor(button2);
		
		TextButton button3 = new TextButton("AI VS AI", MainGame.skin, "arial");
		table.addActor(button3);
		
		table.space(30).center();
		//table.debugAll();
		table.pack();
		
		button1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.input.setInputProcessor(null);
				Menu.this.dispose();
				new NormalGame(mainGame, Players.PERSON, Players.PERSON);
			}
		});
		
		button2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.input.setInputProcessor(null);
				Menu.this.dispose();
				new NormalGame(mainGame, Players.PERSON, Players.AI);
			}
		});
		
		button3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.input.setInputProcessor(null);
				Menu.this.dispose();
				new NormalGame(mainGame, Players.AI, Players.AI);
			}
		});
		
		/*new Dialog("Test", MainGame.skin, "dialog") {
			protected void result (Object object)
			{

			}
		}.text("TEST").button("Ok", true).key(Keys.ENTER, true).show(stage);*/
		
		mainGame.setScreen(this);
	}

	@Override
	public void resize (int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}