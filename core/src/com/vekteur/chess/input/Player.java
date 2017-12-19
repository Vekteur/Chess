package com.vekteur.chess.input;

import com.badlogic.gdx.math.Vector3;
import com.vekteur.chess.model.Move;

public interface Player {
	public enum State
	{
		DEFAULT, VISUALIZATION, SELECTION
	}
	
	public State getState();
	public Vector3 getSelectedPiecePos();
	public Vector3 getSelectionPos();
	public Move getMove();
}
