package me.naftoreiclag.fileparsecommons;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ParseCommons
{
	// collision data[][] -> ByteBuffer
	// ByteBuffer -> collision data[][]

	// ByteBuffer -> alphaed byte[][]
	// ByteBuffer -> unalphaed byte[][]
	// alphaed/unalphaed byte[][] -> Image

	// Image -> alphaed byte[][]
	// Image -> unalphaed byte[][]
	// alphaed byte[][] -> ByteBuffer
	// unalphaed byte[][] -> ByteBuffer
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};

	public static BufferedImage parseUnalphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
	
		int ehx = 0;
		int why = 0;
		
		while(true)
		{
			byte colorStrip = data.get();
			
			byte color = (byte) (colorStrip & 0x07);
			int width = (colorStrip & 0xff) >> 3;
			
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

	public static BufferedImage parseUnalphaedImage(ByteBuffer data)
	{
		int pWidth = data.get() & 0xFF;
		int pHeight = data.get() & 0xFF;
		
		return parseUnalphaedImage(data, pWidth, pHeight);
	}

	public static BufferedImage parseChunkImage(ByteBuffer data)
	{
		return parseUnalphaedImage(data, 128, 128);
	}

	public static BufferedImage parseAlphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);

		int ehx = 0;
		int why = 0;
		while(true)
		{
			byte colorStrip = data.get();
			
			byte color;
			int width;
			
			if((colorStrip & 0x80) > 0)
			{
				color = 8;
				width = colorStrip & 0x7F;
			}
			else
			{
				color = (byte) (colorStrip & 0x07);
				width = (colorStrip & 0xff) >> 3;
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

	public static BufferedImage parseAlphaedImage(ByteBuffer data)
	{
		int pWidth = data.get() & 0xFF;
		int pHeight = data.get() & 0xFF;
		
		return parseAlphaedImage(data, pWidth, pHeight);
	}
}
