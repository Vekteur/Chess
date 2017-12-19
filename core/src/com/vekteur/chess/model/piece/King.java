package com.vekteur.chess.model.piece;

import java.util.ArrayList;


import com.vekteur.chess.utils.Pos;
import com.vekteur.chess.model.Map;
import com.vekteur.chess.utils.Dir;

public class King extends Piece {

	public King(Pos _pos, boolean _white, Map _map) {
		super(_pos, _white, _map);
	}

	@Override
	public ArrayList<Pos> getReachablePos() {
		ArrayList<Pos> points = new ArrayList<Pos>();
		for(Dir dir : Dir.allDirs())
		{
			Pos target = pos.add(dir.getPoint());
			if(this.canMove(target))
				points.add(target);
		}
		return points;
	}
	
	static int value = 900;
	
	static double[][] values = new double[][]
	{
	    {3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    {3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    {3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    {3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    {2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
	    {1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
	    {2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0},
	    {2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0}
	};
	
	public double getValue()
	{
		Pos p = getRelativePos();
		return value + values[p.x][p.y];
	}
}