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
		
		return parseUnalphaedImage(data, byteIndex, pWidth, pHeight);
	}

	public static BufferedImage parseChunkImage(byte[] data, int byteIndex)
	{
		return parseUnalphaedImage(data, byteIndex, 128, 128);
	}

	public static BufferedImage parseUnalphaedImage(byte[] data, int byteIndex, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);

		int ehx = 0;
		int why = 0;
		
		for(; byteIndex < data.length; ++ byteIndex)
		{
			byte color = (byte) (data[byteIndex] & 0x07);
			int width = (data[byteIndex] & 0xff) >> 3;
			
			for(int x = 0; x < width; ++ x)
			{
				ret.setRGB(ehx ++, why, pallete[color] | 0xFF000000);

				if(ehx >= pWidth)
				{
					ehx = 0;
					++ why;
					
					if(why >= pHeight)
					{
						break;
					}
				}
			}
			
			if(why >= pHeight)
			{
				break;
			}
		}
		
		return ret;
	}

	public static BufferedImage parseAlphaedImage(byte[] data, int byteIndex)
	{
		int pWidth = data[byteIndex ++] & 0xFF;
		int pHeight = data[byteIndex ++] & 0xFF;
		
		return parseAlphaedImage(data, byteIndex, pWidth, pHeight);
	}

	public static BufferedImage parseAlphaedImage(byte[] data, int byteIndex, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);

		int ehx = 0;
		int why = 0;
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
				if(color == 8)
				{
					ret.setRGB(ehx ++, why, 0);
				}
				else
				{
					ret.setRGB(ehx ++, why, pallete[color] | 0xFF000000);
				}
	
				if(ehx >= pWidth)
				{
					ehx = 0;
					++ why;
					
					if(why >= pHeight)
					{
						break;
					}
				}
			}
			
			if(why >= pHeight)
			{
				break;
			}
		}
		
		return ret;
	}
}
