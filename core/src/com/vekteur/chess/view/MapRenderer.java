package com.vekteur.chess.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.vekteur.chess.MainGame;
import com.vekteur.chess.Menu;
import com.vekteur.chess.NormalGame;
import com.vekteur.chess.input.MouseInput;
import com.vekteur.chess.input.MouseInput.Players;
import com.vekteur.chess.input.Player.State;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.model.Move;
import com.vekteur.chess.model.piece.Piece;
import com.vekteur.chess.utils.Pos;

public class MapRenderer implements Screen {
	
  	private final MainGame mainGame;
  	private NormalGame game;
  	private Map map;
	private OrthographicCamera camera;
	private MouseInput input;
	public HashMap<String, Texture> textures;
	private Stage stage;

	public MapRenderer(MainGame _mainGame, NormalGame _game, Map _map, Players player1, Players player2)
	{
		mainGame = _mainGame;
		game = _game;
		map = _map;
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		camera = new OrthographicCamera();
		textures = new HashMap<String, Texture>();
		for(FileHandle file : Gdx.files.internal("assets/ChessPieces/").list())
		{
			textures.put(file.nameWithoutExtension(), new Texture(file));
		}
		input = new MouseInput(map, this, player1, player2);
	}
	
	public OrthographicCamera getCamera()
	{
		return camera;
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.65f, 0.65f, 0.65f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		input.process(delta);
		
		renderGrid();
		renderPieces();
		
		stage.act();
		stage.draw();
	}
	
	public void renderGrid()
	{
		Pos selectionPos = null;
		if(input.getPlayer().getState() == State.SELECTION)
			selectionPos = MouseInput.v3ToPos(input.getPlayer().getSelectionPos());
		
		MainGame.shapeRenderer.setProjectionMatrix(camera.combined);
		MainGame.shapeRenderer.begin(ShapeType.Filled);
		for(int x = 0; x < Map.SIDE; ++x)
			for(int y = 0; y < Map.SIDE; ++y)
			{
				Pos pos = new Pos(x, y);
				Move lastMove = map.getLastMove();
				Color color = null;
				if((x + y) % 2 == 0)
					color = new Color(0.8f, 0.8f, 0.8f, 1.0f);
				else
					color = new Color(0.5f, 0.5f, 0.5f, 1.0f);
				if(pos.equals(selectionPos))
				{
					MainGame.shapeRenderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
					MainGame.shapeRenderer.rect(x, y, 1, 1);
					MainGame.shapeRenderer.setColor(color);
					MainGame.shapeRenderer.rect(x + 0.08f, y + 0.08f, 0.84f, 0.84f);
				}
				else if(lastMove != null && (pos.equals(lastMove.from) || pos.equals(lastMove.to)))
				{
					MainGame.shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1.0f);
					MainGame.shapeRenderer.rect(x, y, 1, 1);
					MainGame.shapeRenderer.setColor(color);
					MainGame.shapeRenderer.rect(x + 0.08f, y + 0.08f, 0.84f, 0.84f);
				}
				else
				{
					MainGame.shapeRenderer.setColor(color);
					MainGame.shapeRenderer.rect(x, y, 1, 1);
				}	
			}
		MainGame.shapeRenderer.end();
	}
	
	public void renderPieces()
	{
		if(input.getPlayer().getState() == State.SELECTION || input.getPlayer().getState() == State.VISUALIZATION)
		{
			Pos piecePos = MouseInput.v3ToPos(input.getPlayer().getSelectedPiecePos());
			for(Pos reachablePos : map.getAllReachableAllyPos().get(piecePos))
			{
				MainGame.shapeRenderer.setProjectionMatrix(camera.combined);
				MainGame.shapeRenderer.begin(ShapeType.Filled);
				Gdx.gl20.glEnable(GL20.GL_BLEND);
				MainGame.shapeRenderer.setColor(1f, 1f, 0f, 0.3f);
				MainGame.shapeRenderer.rect(reachablePos.x, reachablePos.y, 1, 1);
				MainGame.shapeRenderer.end();
			}
		}
		
		MainGame.spriteBatch.setProjectionMatrix(camera.combined);
		MainGame.spriteBatch.begin();
		for(int x = 0; x < Map.SIDE; ++x)
			for(int y = 0; y < Map.SIDE; ++y)
			{
				Pos pos = new Pos(x, y);
				if(input.getPlayer().getState() == State.SELECTION && pos.equals(MouseInput.v3ToPos(input.getPlayer().getSelectedPiecePos())))
				{
					Piece piece = map.getPiece(pos);
					MainGame.spriteBatch.draw(textures.get(piece.toString()), input.getPlayer().getSelectionPos().x - 0.5f, input.getPlayer().getSelectionPos().y - 0.5f, 1, 1);
				}
				else
				{
					Piece piece = map.getPiece(pos);
					if(piece != null)
					{
						assert textures.get(piece.toString()) != null;
						MainGame.spriteBatch.draw(textures.get(piece.toString()), x, y, 1, 1);
					}
				}
			}
		MainGame.spriteBatch.end();
	}
	
	public void showEndScreen()
	{
		new Dialog("CheckMate", MainGame.skin, "dialog") {
			protected void result (Object object)
			{
				if(object.equals(true))
				{
					MapRenderer.this.dispose();
					Gdx.input.setInputProcessor(null);
					mainGame.setScreen(new Menu(mainGame));
				}
			}
		}.text("Player " + (map.isWhiteTurn() ? "Black" : "White") + " won").button("Ok", true).key(Keys.ENTER, true).show(stage);
	}

	@Override
	public void resize(int width, int height) {
		System.out.println(width + " " + height);
		stage.getViewport().update(width, height, true);
		float w = width;
		float h = height;
		
		camera.setToOrtho(false, Math.max(Map.SIDE, Map.SIDE * (w / h)), Math.max(Map.SIDE, Map.SIDE * (h / w)));
		camera.position.set(Map.SIDE / 2, Map.SIDE / 2, 0);
		camera.update();
		System.out.println(camera.viewportWidth + " " + camera.viewportHeight);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}
}
