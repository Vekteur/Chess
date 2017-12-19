package com.vekteur.chess.model;

import com.vekteur.chess.utils.Pos;

public class Move
{		
	public Pos from, to;
	
	public Move(Pos _from, Pos _to)
	{
		from = _from;
		to = _to;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Move))
			return false;
		Move move = (Move) o;
		return from == move.from && to == move.to;
	}
}