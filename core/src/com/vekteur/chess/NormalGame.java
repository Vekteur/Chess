package com.vekteur.chess;

import com.badlogic.gdx.Gdx;
import com.vekteur.chess.input.MouseInput.Players;
import com.vekteur.chess.model.Map;

import com.vekteur.chess.view.MapRenderer;

public class NormalGame {
  	private final MainGame mainGame;
  	private Map map;
  	private MapRenderer mapRenderer;

	public NormalGame(final MainGame _mainGame, Players player1, Players player2) {
		mainGame = _mainGame;
		
		map = new Map(mainGame, this);
		mapRenderer = new MapRenderer(mainGame, this, map, player1, player2);
		mainGame.setScreen(mapRenderer);
		mapRenderer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
}
