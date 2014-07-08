package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Landmark
{
	protected final int tWidth;
	protected final int tHeight;
	
	protected final boolean[][] collision;
	
	protected final BufferedImage image;
	
	public Landmark(ByteBuffer data)
	{
		tWidth = data.get() & 0xFF;
		tHeight = data.get() & 0xFF;
		
		collision = new boolean[tWidth][tHeight];
		
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
		
		image = ParseCommons.alphaedBufferToImage(data, tWidth << 3, tHeight << 3);
	}
}
