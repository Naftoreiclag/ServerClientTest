package me.naftoreiclag.servertwo;

// struct
public class Location
{
	protected long cId;
	
	protected int x;
	protected int y;
	
	protected Location(int cId, int x, int y)
	{
		this.cId = cId;
		
		this.x = x;
		this.y = y;
	}
}
