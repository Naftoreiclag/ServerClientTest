package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;

public class Decal
{
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public BufferedImage getImage()
	{
		return null;
	}

	public static BufferedImage parseUnalphaedImage(byte[] data, int byteIndex)
	{
		int pWidth = data[byteIndex ++] & 0xFF;
		int pHeight = data[byteIndex ++] & 0xFF;
		
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);

		int ehx = 0;
		int why = 0;
		
		for(; byteIndex < data.length; ++ byteIndex)
		{
			byte color = (byte) (data[byteIndex] & 0x07);
			int width = (data[byteIndex] & 0xff) >> 3;
			
			for(int x = 0; x < width; ++ x)
			{
				try
				{
					ret.setRGB(ehx ++, why, pallete[color] | 0xFF000000);
				}
				catch(Exception e)
				{
					System.out.println((ehx - 1) + " " + why);
				}

				if(ehx >= pWidth)
				{
					ehx = 0;
					++ why;
				}
			}
		}
		
		return ret;
	}
}
