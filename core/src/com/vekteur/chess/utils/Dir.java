package com.vekteur.chess.utils;

import com.vekteur.chess.utils.Pos;

public enum Dir {
	UP(new Pos(0, 1)),
	UPRIGHT(new Pos(1, 1)),
	RIGHT(new Pos(1, 0)),
	DOWNRIGHT(new Pos(1, -1)),
	DOWN(new Pos(0, -1)),
	DOWNLEFT(new Pos(-1, -1)),
	LEFT(new Pos(-1, 0)),
	UPLEFT(new Pos(-1, 1));
	
	private Pos dir;
	
	Dir(Pos _dir)
	{
		this.dir = _dir;
	}
	
	public Pos getPoint()
	{
		return dir;
	}
	
	public Dir rotate90()
	{
		return Dir.values()[(ordinal() + 1) % Dir.values().length];
	}
	
	public Dir opp()
	{
		return Dir.values()[(ordinal() + 2) % Dir.values().length];
	}
	
	public static Dir[] adjDirs()
	{
		Dir[] dirs = new Dir[Dir.values().length / 2];
		for(int i = 0; i < Dir.values().length / 2; ++i)
			dirs[i] = Dir.values()[i * 2];
		return dirs;
	}
	
	public static Dir[] diagDirs()
	{
		Dir[] dirs = new Dir[Dir.values().length / 2];
		for(int i = 0; i < Dir.values().length / 2; ++i)
			dirs[i] = Dir.values()[i * 2 + 1];
		return dirs;
	}
	
	public static Dir[] allDirs()
	{
		return Dir.values();
	}
}