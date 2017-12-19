package com.vekteur.chess.utils;

import com.badlogic.gdx.math.GridPoint2;
import com.vekteur.chess.utils.Pos;

public class Pos extends GridPoint2 {
	
	private static final long serialVersionUID = 1L;

	public Pos(int x, int y)
	{
		super(x, y);
	}
	
	public Pos(Pos pos)
	{
		super(pos.x, pos.y);
	}
	
	public Pos add(Pos pos)
	{
		return new Pos(x + pos.x, y + pos.y);
	}
	
	public Pos sub(Pos pos)
	{
		return new Pos(x - pos.x, y - pos.y);
	}
}
