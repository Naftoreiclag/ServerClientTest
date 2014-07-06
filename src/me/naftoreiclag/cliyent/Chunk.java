package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;

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
	
	public Chunk(byte[] data, int byteIndex)
	{
		byte format = data[byteIndex ++];
		areaId = data[byteIndex ++];
		id = data[byteIndex ++];
		wId = data[byteIndex ++];
		nId = data[byteIndex ++];
		eId = data[byteIndex ++];
		sId = data[byteIndex ++];
		
		int colX = 0;
		int colY = 0;
		for(; byteIndex < data.length; ++ byteIndex)
		{
			for(int i = 0; i < 8; ++ i)
			{
				collision[colX ++][colY] = (data[byteIndex] & (1 << i)) > 0;
				
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
		
		image = Decal.parse(data, byteIndex);
	}
}
