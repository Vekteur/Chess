package com.vekteur.chess.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.model.Move;
import com.vekteur.chess.utils.Pos;
import com.vekteur.chess.view.MapRenderer;
		
/*if(!map.isWhiteTurn() || aiTurn)
//if(true)
{
	if(!waitingThread && !aiTurn)
	{
		waitingThread = true;
		aiTurn = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				aiMove = chessAI.getMove();
				waitingThread = false;
			}
		}).start();
	}*/

public class MouseInput
{
	private Map map;
	private MapRenderer mapRenderer;
	private Player whitePlayer, blackPlayer;
	private boolean ended = false;
	
	public enum Players { PERSON, AI };
	
	public MouseInput(Map _map, MapRenderer _mapRenderer, Players player1, Players player2)
	{
		map = _map;
		mapRenderer = _mapRenderer;
		
		int difficulty = (Gdx.app.getType() == ApplicationType.Android ? 2 : 3);
		
		if(player1 == Players.PERSON)
			whitePlayer = new Person(map, mapRenderer);
		else if(player1 == Players.AI)
			whitePlayer = new ChessAI(map, difficulty);
		
		if(player2 == Players.PERSON)
			blackPlayer = new Person(map, mapRenderer);
		else if(player2 == Players.AI)
			blackPlayer = new ChessAI(map, difficulty);	
	}
	
	public void process(float delta)
	{
		if(!ended)
		{
			if(map.isWhiteTurn())
			{
				Move move = whitePlayer.getMove();
				if(move != null)
					map.movePiece(move.from, move.to, true, true);
			}
			else if(!map.isWhiteTurn())
			{
				Move move = blackPlayer.getMove();
				if(move != null)
					map.movePiece(move.from, move.to, true, true);
			}
		}
		
		if(map.isCheckMate() && !ended)
		{
			ended = true;
			mapRenderer.showEndScreen();
		}
	}
	
	public Player getPlayer()
	{
		return map.isWhiteTurn() ? whitePlayer : blackPlayer;
	}
	
	public static Pos v3ToPos(Vector3 v)
	{
		if(v == null)
			return null;
		return new Pos((int)Math.floor(v.x), (int)Math.floor(v.y));
	}
}