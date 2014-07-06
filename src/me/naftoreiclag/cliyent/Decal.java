package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;

public class Decal
{
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public BufferedImage getImage()
	{
		return null;
	}

	public static BufferedImage parse(byte[] data, int byteIndex)
	{
		int pWidth = data[byteIndex ++];
		int pHeight = data[byteIndex ++];
		
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);

		int pixX = 0;
		int pixY = 0;
		for(; byteIndex < data.length; ++ byteIndex)
		{
			byte color;
			int width;
			
			if((data[byteIndex] & 0x80) > 0)
			{
				color = 8;
				width = data[byteIndex] & 0x7F;
			}
			else
			{
				color = (byte) (data[byteIndex] & 0x07);
				width = (data[byteIndex] & 0xff) >> 3;
			}
			
			for(int x = 0; x < width; ++ x)
			{
				ret.setRGB(pixX ++, pixY, pallete[color]);
	
				if(pixX >= pWidth)
				{
					pixX = 0;
					++ pixY;
					
					if(pixY >= pHeight)
					{
						break;
					}
				}
			}
			
			if(pixY >= pHeight)
			{
				break;
			}
		}
		
		return ret;
	}
}
