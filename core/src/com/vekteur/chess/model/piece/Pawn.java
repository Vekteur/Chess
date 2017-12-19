package com.vekteur.chess.model.piece;

import java.util.ArrayList;

import com.vekteur.chess.model.Map;
import com.vekteur.chess.utils.Dir;
import com.vekteur.chess.utils.Pos;

public class Pawn extends Piece {
	
	boolean firstMove = true;

	public Pawn(Pos _pos, boolean _white, Map _map) {
		super(_pos, _white, _map);
	}

	@Override
	public ArrayList<Pos> getReachablePos() {
		ArrayList<Pos> points = new ArrayList<Pos>();
		Dir frontDir = isWhite() ? Dir.UP : Dir.DOWN;
		Pos frontTarget = pos.add(frontDir.getPoint());
		if(map.isValid(frontTarget) && !map.containsPiece(frontTarget))
		{
			points.add(frontTarget);
			Pos frontTarget2 = frontTarget.add(frontDir.getPoint());
			if(moves == 0 && map.isValid(frontTarget2) && !map.containsPiece(frontTarget2))
			{
				if(this.canMove(frontTarget2))
					points.add(frontTarget2);
			}
		}
		for(Dir sideDir : new Dir[]{Dir.LEFT, Dir.RIGHT})
		{
			Pos sideTarget = frontTarget.add(sideDir.getPoint());
			if(map.containsPiece(sideTarget) && !this.isSameColor(map.getPiece(sideTarget)))
				points.add(sideTarget);
		}

		return points;
	}
	
	@Override
	public void move(Pos pos, boolean newMove)
	{
		int up = white ? Map.SIDE - 1 : 0;
		if(pos.y == up)
		{
			map.addPiece(this.pos, new Queen(pos, white, map));
			assert map.getPiece(this.pos) instanceof Queen;
		}
		super.move(pos, newMove);
	}
	
	static int value = 10;
	
	static double[][] values = new double[][]
	{
        {7.0,  7.0,  8.0,  9.0,  9.0,  8.0,  7.0,  7.0},
        {2.0,  2.0,  3.0,  4.0,  4.0,  3.0,  2.0,  2.0},
        {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
        {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
        {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
        {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
        {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
	};
	
	public double getValue()
	{
		Pos p = getRelativePos();
		return value + values[p.x][p.y];
	}
}