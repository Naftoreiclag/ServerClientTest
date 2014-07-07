package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;

public class Landmark
{
	protected final int tWidth;
	protected final int tHeight;
	
	protected final boolean[][] collision;
	
	protected final BufferedImage image;
	
	public Landmark(byte[] data, int byteIndex)
	{
		tWidth = data[byteIndex ++];
		tHeight = data[byteIndex ++];
		
		collision = new boolean[tWidth][tHeight];
		
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
		++ byteIndex;
		
		image = Decal.parseAlphaedImage(data, byteIndex, tWidth << 3, tHeight << 3);
	}
}
