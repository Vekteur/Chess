package com.vekteur.chess.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.vekteur.chess.MainGame;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.model.Move;
import com.vekteur.chess.utils.Pos;
import com.vekteur.chess.view.MapRenderer;

public class Person implements Player {
	
	private Map map;
	private MapRenderer mapRenderer;
	private State state = State.DEFAULT;
	private Vector3 selectedPiecePos, selectionPos, lastTouchPos;
	private boolean clickUndo = false;
	
	public Person(Map _map, MapRenderer _mapRenderer)
	{
		map = _map;
		mapRenderer = _mapRenderer;
	}
	
	public Move getMove()
	{
		if(Gdx.input.isKeyPressed(Keys.U))
		{
			if(!clickUndo)
			{
				MainGame.logger.debug("UNDO");
				map.undo(true);
				map.undo(true);
				clickUndo = true;
			}
		}
		else
		{
			clickUndo = false;
		}
		
		Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		mousePos = mapRenderer.getCamera().unproject(mousePos);
		if (Gdx.input.isTouched())
		{
			Vector3 touchPos = mousePos;
			lastTouchPos = touchPos;
			Pos gridPos = v3ToPos(touchPos);
			
			if(state != State.SELECTION && map.containsPiece(gridPos) && map.getPiece(gridPos).isWhite() == map.isWhiteTurn())
			{
				state = State.SELECTION;
				selectedPiecePos = touchPos;
			}
				
			if(state == State.SELECTION)
			{
				selectionPos = touchPos;
			}
		}
		else
		{
			if(state == State.SELECTION)
			{
				state = State.DEFAULT;
				Pos targetPos = new Pos((int)Math.floor(lastTouchPos.x), (int)Math.floor(lastTouchPos.y));
				
				// The initial selection is valid
				Pos piecePos = v3ToPos(selectedPiecePos);
				// Check if the target selection is valid
				boolean valid = false;
				for(Pos reachablePos : map.getAllReachableAllyPos().get(piecePos))
				{
					if(targetPos.equals(reachablePos))
						valid = true;
				}
				// If the target selection is valid, move
				if(valid)
				{
					return new Move(v3ToPos(selectedPiecePos), targetPos);
				}	
			}
			else
			{
				if(map.containsPiece(v3ToPos(mousePos)) && map.getPiece(v3ToPos(mousePos)).isWhite() == map.isWhiteTurn())
				{
					state = State.VISUALIZATION;
					selectedPiecePos = mousePos;
				}
				else
				{
					state = State.DEFAULT;
				}
			}
		}
		return null;
	}
		
	public State getState()
	{
		return state;
	}
	
	public static Pos v3ToPos(Vector3 v)
	{
		if(v == null)
			return null;
		return new Pos((int)Math.floor(v.x), (int)Math.floor(v.y));
	}
	
	public Vector3 getSelectedPiecePos()
	{
		return selectedPiecePos;
	}
	
	public Vector3 getSelectionPos()
	{
		return selectionPos;
	}
}
