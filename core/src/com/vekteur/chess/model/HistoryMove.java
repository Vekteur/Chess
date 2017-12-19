package com.vekteur.chess.model;

import com.vekteur.chess.model.piece.Piece;
import com.vekteur.chess.utils.Pos;

public class HistoryMove {
	
	public Pos from, to;
	public Piece movedPiece, caughtPiece;
	public int repeat = 0;
	
	public HistoryMove(Pos _from, Pos _to, Piece _movedPiece, Piece _caughtPiece, int _repeat)
	{
		from = _from;
		to = _to;
		movedPiece = _movedPiece;
		caughtPiece = _caughtPiece;
		repeat = _repeat;
	}
}
