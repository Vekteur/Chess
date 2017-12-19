package com.vekteur.chess.model.piece;

import java.util.ArrayList;

import com.vekteur.chess.utils.Pos;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.utils.Dir;

public class Rook  extends Piece {

	public Rook(Pos _pos, boolean _white, Map _map) {
		super(_pos, _white, _map);
	}

	@Override
	public ArrayList<Pos> getReachablePos() {
		ArrayList<Pos> points = new ArrayList<Pos>();
		for(Dir dir : Dir.adjDirs())
		{
			Pos target = pos.add(dir.getPoint());
			while(this.canMove(target))
			{
				points.add(target);
				if(map.containsPiece(target))
					break;
				target = target.add(dir.getPoint());
			}
		}
		return points;
	}
	
	static int value = 50;
	
	static double[][] values = new double[][]
	{
	    { 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
	    { 0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    { 0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
	};
	
	public double getValue()
	{
		Pos p = getRelativePos();
		return value + values[p.x][p.y];
	}
}