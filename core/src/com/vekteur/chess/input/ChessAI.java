package com.vekteur.chess.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector3;
import com.vekteur.chess.MainGame;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.model.Move;
import com.vekteur.chess.utils.Pos;

public class ChessAI implements Player {
	
	private Map map;
	private int recursions = 0;
	private boolean whiteTurn = false;
	private int depth = 3;
	private State state = State.DEFAULT;
	
	public ChessAI(Map _map, int _depth)
	{
		map = _map;
		depth = _depth;
	}
	
	@Override
	public Move getMove()
	{
		recursions = 0;
		long startTime = System.nanoTime();
		whiteTurn = map.isWhiteTurn();
		Move bestMove = minimaxRoot(depth, false);
		long totalTime = System.nanoTime() - startTime;
		double timeInSec = totalTime / 1e9;
		MainGame.logger.debug("Recursions : " + Integer.toString(recursions));
		MainGame.logger.debug("Time : " + timeInSec + "s");
		return bestMove;
	}
	
	private ArrayList<Move> getAllMoves()
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		for(Entry<Pos, ArrayList<Pos>> entry : map.getAllReachableAllyPos().entrySet())
		{
			Pos from = entry.getKey();
			for(Pos to : entry.getValue())
			{
				moves.add(new Move(from, to));
			}
		}
		Collections.shuffle(moves);
		return moves;
	}
	
	private Move minimaxRoot(int depth, boolean isMaximising)
	{
		double bestValue = -10001;
		Move bestMove = null;
		for(Move move : getAllMoves())
		{
			map.movePiece(move.from, move.to, true, true);
			double value = minimax(depth - 1, -10000, 10000, isMaximising);
			map.undo(true);
			if(value > bestValue)
			{
				bestValue = value;
				bestMove = move;
			}
		}
		return bestMove;
	}
	static boolean[] depths = new boolean[5];
	private double minimax(int depth, double alpha, double beta, boolean isMaximising)
	{
		if(!depths[depth])
		{
			depths[depth] = true;
			System.out.println(depth + " " + isMaximising);
		}
		++recursions;
		if(depth == 0)
			return whiteTurn ? map.evaluate() : -map.evaluate();
		
		if(isMaximising)
		{
			double bestValue = -10000;
			for(Move move : getAllMoves())
			{
				map.movePiece(move.from, move.to, true, depth != 1);
				bestValue = Math.max(bestValue, minimax(depth - 1, alpha, beta, !isMaximising));
				map.undo(true);
				alpha = Math.max(alpha, bestValue);
				if(beta <= alpha)
					return bestValue;
			}
			return bestValue;
		}
		else
		{
			double bestValue = 10000;
			for(Move move : getAllMoves())
			{
				map.movePiece(move.from, move.to, true, depth != 1);
				bestValue = Math.min(bestValue, minimax(depth - 1, alpha, beta, !isMaximising));
				map.undo(true);
				beta = Math.min(beta, bestValue);
				if(beta <= alpha)
					return bestValue;
			}
			return bestValue;
		}
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public Vector3 getSelectedPiecePos() {
		return null;
	}

	@Override
	public Vector3 getSelectionPos() {
		return null;
	}
}
