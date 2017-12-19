package com.vekteur.chess.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.vekteur.chess.MainGame;
import com.vekteur.chess.NormalGame;
import com.vekteur.chess.model.piece.Bishop;
import com.vekteur.chess.model.piece.King;
import com.vekteur.chess.model.piece.Knight;
import com.vekteur.chess.model.piece.Pawn;
import com.vekteur.chess.model.piece.Piece;
import com.vekteur.chess.model.piece.Queen;
import com.vekteur.chess.model.piece.Rook;
import com.vekteur.chess.utils.Pos;

public class Map {

	public static final int SIDE = 8;
	
	private final MainGame mainGame;
	private final NormalGame game;
	private Piece[][] grid = new Piece[SIDE][SIDE];
	private Stack<HistoryMove> history = new Stack<HistoryMove>();
	private HashMap<Pos, ArrayList<Pos>> allReachableAllyPos = new HashMap<Pos, ArrayList<Pos>>();
	private boolean checkMate = false;
	private boolean whiteTurn = true;
	
	
	public Map(final MainGame _mainGame, final NormalGame _game)
	{
		mainGame = _mainGame;
		game = _game;
		
		addPieceEachSide(new Pos(0, 0), Rook.class);
		addPieceEachSide(new Pos(1, 0), Knight.class);
		addPieceEachSide(new Pos(2, 0), Bishop.class);
		addPieceEachSide(new Pos(3, 0), Queen.class);
		addPieceEachSide(new Pos(4, 0), King.class);
		addPieceEachSide(new Pos(5, 0), Bishop.class);
		addPieceEachSide(new Pos(6, 0), Knight.class);
		addPieceEachSide(new Pos(7, 0), Rook.class);
		
		for(int x = 0; x < SIDE; ++x)
			addPieceEachSide(new Pos(x, 1), Pawn.class);
		
		updateAllReachableAllyPos();
	}
	
	public void addPiece(Pos pos, Piece piece)
	{
		grid[pos.x][pos.y] = piece;
	}
	
	public Piece getPiece(Pos pos)
	{
		if(!this.isValid(pos))
			return null;
		return grid[pos.x][pos.y];
	}
	
	public void movePiece(Pos from, Pos to, boolean updateHistory, boolean updateReachablePos)
	{
		if(updateHistory)
		{
			HistoryMove lastAllyMove = this.getLastAllyMove();
			boolean same = false;
			if(lastAllyMove != null && to.equals(lastAllyMove.from) && from.equals(lastAllyMove.to))
				same = true;
			history.push(new HistoryMove(from, to, this.getPiece(from), this.getPiece(to), same ? lastAllyMove.repeat + 1 : 0));
		}
		grid[from.x][from.y].move(to, updateHistory);
		grid[to.x][to.y] = grid[from.x][from.y];
		grid[from.x][from.y] = null;
		whiteTurn = !whiteTurn;
			
		if(updateReachablePos)
			updateAllReachableAllyPos();
	}
	
	public void undo(boolean updateReachablePos)
	{
		if(!history.empty())
		{
			HistoryMove lastMove = history.pop();
			addPiece(lastMove.to, lastMove.movedPiece);
			movePiece(lastMove.to, lastMove.from, false, updateReachablePos);
			addPiece(lastMove.to, lastMove.caughtPiece);
		}
	}
	
	public Move getLastMove()
	{
		if(history.empty())
			return null;
		HistoryMove historyMove = history.lastElement();
		return new Move(historyMove.from, historyMove.to);
	}
	
	public HistoryMove getLastAllyMove()
	{
		if(history.size() < 2)
			return null;
		return history.get(history.size() - 2);
	}
	
	public void updateAllReachableAllyPos()
	{
		checkMate = true;
		allReachableAllyPos.clear();
		for(int x = 0; x < SIDE; ++x)
			for(int y = 0; y < SIDE; ++y)
			{
				Pos allyPos = new Pos(x, y);
				if(this.containsPiece(allyPos) && this.getPiece(allyPos).isWhite() == this.isWhiteTurn())
				{
					ArrayList<Pos> reachablePos = this.getPiece(allyPos).getReachablePosWithSafeKing();
					if(!reachablePos.isEmpty())
						checkMate = false;
					allReachableAllyPos.put(allyPos, reachablePos);
				}
			}
	}
	
	public HashMap<Pos, ArrayList<Pos>> getAllReachableAllyPos()
	{
		return allReachableAllyPos;
	}
	
	public void addPieceEachSide(Pos pos, Class<? extends Piece> piece)
	{
		try {
			Constructor<? extends Piece> ct = piece.getConstructor(new Class[]{Pos.class, boolean.class, Map.class});
			addPiece(pos, ct.newInstance(pos, true, this));
			Pos oppPos = new Pos(SIDE - 1, SIDE - 1).sub(pos);
			addPiece(oppPos, ct.newInstance(oppPos, false, this));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValid(Pos pos)
	{
		return pos.x >= 0 && pos.x < SIDE && pos.y >= 0 && pos.y < SIDE;
	}
	
	public boolean containsPiece(Pos pos)
	{
		return isValid(pos) && grid[pos.x][pos.y] != null;
	}
	
	public Piece getKing(boolean white)
	{
		for(int x = 0; x < SIDE; ++x)
			for(int y = 0; y < SIDE; ++y)
			{
				Pos pos = new Pos(x, y);
				if(this.containsPiece(pos) && this.getPiece(pos) instanceof King && this.getPiece(pos).isWhite() == white)
					return this.getPiece(pos);
			}
		return null;
	}
	
	public boolean isWhiteTurn()
	{
		return whiteTurn;
	}
	
	public boolean isCheckMate()
	{
		return checkMate;
	}
	
	public double evaluate()
	{
		double res = 0;
		for(int x = 0; x < SIDE; ++x)
			for(int y = 0; y < SIDE; ++y)
			{
				Pos pos = new Pos(x, y);
				if(this.containsPiece(pos))
				{
					double value = this.getPiece(pos).getValue();
					res += this.getPiece(pos).isWhite() ? value : -value;
				}
			}
		return res;
	}
}
