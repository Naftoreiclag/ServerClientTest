package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Chunk
{
	protected final int localX;
	protected final int localY;
	
	protected final BufferedImage image;
	
	protected final long id;
	protected final long areaId;
	protected final boolean[][] collision;

	protected final long wId;
	protected final long nId;
	protected final long eId;
	protected final long sId;
	
	public Chunk(int x, int y, ByteBuffer data)
	{
		ChunkPool.addChunk(this);
		
		localX = x;
		localY = y;
		
		id = data.getLong();
		areaId = data.getLong();
		wId = data.getLong();
		nId = data.getLong();
		eId = data.getLong();
		sId = data.getLong();
		
		collision = ParseCommons.readCollisionArray(data, 16, 16);
		image = ParseCommons.readUnalphaedImage(data, 128, 128);
	}

	public Chunk(int x, int y)
	{
		ChunkPool.addChunk(this);
		
		localX = x;
		localY = y;
		
		id = 0;
		areaId = 0;
		wId = 0;
		nId = 0;
		eId = 0;
		sId = 0;
		
		collision = new boolean[16][16];
		for(int i = 0; i < 16; ++ i)
		{
			for(int j = 0; j < 16; ++ j)
			{
				collision[i][j] = true;
			}
		}
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	}
}
