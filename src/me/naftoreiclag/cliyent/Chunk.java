package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Chunk
{
	protected final long id;
	protected final long areaId;
	protected final boolean[][] collision = new boolean[16][16];

	protected final long wId;
	protected final long nId;
	protected final long eId;
	protected final long sId;
	
	protected final BufferedImage image;
	
	public Chunk(ByteBuffer data)
	{
		id = data.getLong();
		wId = data.getLong();
		nId = data.getLong();
		eId = data.getLong();
		sId = data.getLong();
		areaId = data.getLong();
		
		System.out.println("Chunk is id: " + id + " surrounded by: " + wId + ", " + nId + ", " + eId + ", " + sId);
		
		int colX = 0;
		int colY = 0;
		while(true)
		{
			byte eightTiles = data.get();
			
			for(int i = 0; i < 8; ++ i)
			{
				collision[colX ++][colY] = (eightTiles & (1 << i)) > 0;
				
				if(colX >= 16)
				{
					colX = 0;
					++ colY;
					
					if(colY >= 16)
					{
						break;
					}
				}
			}
			
			if(colY >= 16)
			{
				break;
			}
		}
		
		image = ParseCommons.parseChunkImage(data);
	}
}
