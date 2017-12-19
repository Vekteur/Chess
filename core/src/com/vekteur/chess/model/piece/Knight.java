
package com.vekteur.chess.model.piece;

import java.util.ArrayList;

import com.vekteur.chess.model.Map;
import com.vekteur.chess.utils.Pos;

public class Knight extends Piece {
	
	static final int dep1 = 1;
	static final int dep2 = 2;

	public Knight(Pos _pos, boolean _white, Map _map) {
		super(_pos, _white, _map);
	}

	@Override
	public ArrayList<Pos> getReachablePos() {
		ArrayList<Pos> points = new ArrayList<Pos>();
		for(int dep1 : new int[]{-dep1, dep1})
			for(int dep2 : new int[]{-dep2, dep2})
			{
				Pos target = new Pos(pos.x + dep1, pos.y + dep2);
				if(canMove(target))
					points.add(target);
				target = new Pos(pos.x + dep2, pos.y + dep1);
				if(canMove(target))
					points.add(target);
			}
			
		return points;
	}
	
	static int value = 30;
	
	static double[][] values = new double[][]
	{
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
        {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
        {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
        {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
        {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
        {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
        {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
	};
	
	public double getValue()
	{
		Pos p = getRelativePos();
		return value + values[p.x][p.y];
	}
}