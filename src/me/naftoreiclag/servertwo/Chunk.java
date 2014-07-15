package me.naftoreiclag.servertwo;

import java.nio.ByteBuffer;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Chunk
{
	protected final long id;
	protected final long areaId;
	protected final boolean[][] collision;

	protected final long wId;
	protected final long nId;
	protected final long eId;
	protected final long sId;
	
	public Chunk(ByteBuffer data)
	{
		id = data.getLong();
		areaId = data.getLong();
		wId = data.getLong();
		nId = data.getLong();
		eId = data.getLong();
		sId = data.getLong();
		
		collision = ParseCommons.readCollisionArray(data, 16, 16);
	}
}
