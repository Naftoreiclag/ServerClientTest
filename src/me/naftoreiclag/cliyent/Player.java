package me.naftoreiclag.cliyent;

public class Player
{
	protected final long locationChunkID;
	protected int cx = 0;
	protected int cy = 0;
	
	protected Player(long chunkID)
	{
		this.locationChunkID = chunkID;
	}
}
