package com.vekteur.chess.model.piece;

import java.util.ArrayList;

import com.vekteur.chess.model.HistoryMove;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.utils.Pos;

abstract public class Piece {
	protected boolean white;
	protected Pos pos;
	protected Map map;
	protected int moves = 0;
	
	public Piece(Pos _pos, boolean _white, Map _map)
	{
		map = _map;
		pos = _pos;
		white = _white;
	}
	
	protected Pos mult(Pos p, int v)
	{
		return new Pos(p.x * v, p.y * v);
	}
	
	public void move(Pos pos, boolean newMove)
	{
		this.pos = pos;
		if(newMove) ++moves;
		else --moves;
	}

	public boolean isSameColor(Piece piece)
	{
		return isWhite() == piece.isWhite();
	}
	
	protected boolean canMove(Pos pos)
	{
		if(!map.isValid(pos))
			return false;
		if(map.getPiece(pos) != null)
			return !this.isSameColor(map.getPiece(pos));
		HistoryMove lastAllyMove = map.getLastAllyMove();
		if(lastAllyMove != null && lastAllyMove.repeat >= 2 && this.pos.equals(lastAllyMove.to) && pos.equals(lastAllyMove.from))
			return false;
		return true;
	}

	public abstract ArrayList<Pos> getReachablePos();
	
	public abstract double getValue();
	
	protected Pos getRelativePos()
	{
		return white ? new Pos(Map.SIDE - 1, Map.SIDE - 1).sub(pos) : pos;
	}

	public ArrayList<Pos> getReachablePosWithSafeKing()
	{
		ArrayList<Pos> res = new ArrayList<Pos>();
		for(Pos reachablePos : this.getReachablePos())
		{
			assert map.getPiece(pos) != null;
			map.movePiece(pos, reachablePos, true, false);
			if(!map.getKing(white).isKillable())
			{
				res.add(reachablePos);
			}
			map.undo(false);
			assert map.getPiece(pos) != null;
		}
		return res;
	}
	
	public boolean isKillable()
	{
		for(int x = 0; x < Map.SIDE; ++x)
			for(int y = 0; y < Map.SIDE; ++y)
			{
				Pos opponentPos = new Pos(x, y);
				Piece opponentPiece = map.getPiece(opponentPos);
				if(map.containsPiece(opponentPos) && !this.isSameColor(opponentPiece))
				{
					for(Pos reachablePos : opponentPiece.getReachablePos())
						if(pos.equals(reachablePos))
							return true;
				}
			}
		return false;
	}
	
	public boolean isBlocked()
	{
		boolean[][] reachableOpponentPos = new boolean[Map.SIDE][Map.SIDE];
		for(int x = 0; x < Map.SIDE; ++x)
			for(int y = 0; y < Map.SIDE; ++y)
			{
				Pos opponentPos = new Pos(x, y);
				Piece opponentPiece = map.getPiece(opponentPos);
				if(opponentPiece != null && !this.isSameColor(opponentPiece))
				{
					for(Pos reachablePos : opponentPiece.getReachablePos())
						reachableOpponentPos[reachablePos.x][reachablePos.y] = true;
				}
			}
		for(Pos reachablePos : this.getReachablePos())
		{
			if(!reachableOpponentPos[reachablePos.x][reachablePos.y])
				return false;
		}
		return true;
	}
	
	public boolean isWhite()
	{
		return white;
	}
	
	public String toString()
	{
		return (white ? "white" : "black") + this.getClass().getSimpleName();
	}
}
