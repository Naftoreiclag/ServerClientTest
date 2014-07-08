package me.naftoreiclag.test;

import java.awt.image.BufferedImage;

public class LoadedProject
{
	byte[][] pixelData;

	BufferedImage displayImage;
	
	int cWidth;
	int cHeight;
	
	int pWidth;
	int pHeight;
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};

	public LoadedProject(BufferedImage image)
	{
		
		cWidth = image.getWidth() >> 7;
		cHeight = image.getHeight() >> 7;
		System.out.println("New projected loaded with " + cWidth + " by " + cHeight);
		pWidth = cWidth << 7;
		pHeight = cHeight << 7;
		System.out.println("New projected loaded with " + pWidth + " by " + pHeight);
		
		pixelData = new byte[pWidth][pHeight];

		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(x, y));
			}
		}
		
		displayImage = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		
		
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				displayImage.setRGB(x, y, pallete[pixelData[x][y]] | 0xFF000000);
			}
		}
	}
	
	/*
	public void updateImage(BufferedImage image)
	{
		pixelData = new byte[pWidth][pHeight];

		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(x, y));
			}
		}
		
		displayImage = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		
		
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				displayImage.setRGB(x, y, pallete[pixelData[x][y]]);
			}
		}
	}
	*/

	public static byte getByteFromRGB(int rgb)
	{
		if ((rgb & 0xFF000000) == 0) { return 8; }

		rgb = rgb & 0x00FFFFFF;

		if (rgb < pallete[1])
		{
			return 0;
		}
		else if (rgb < pallete[2])
		{
			return 1;
		}
		else if (rgb < pallete[3])
		{
			return 2;
		}
		else if (rgb < pallete[4])
		{
			return 3;
		}
		else if (rgb < pallete[5])
		{
			return 4;
		}
		else if (rgb < pallete[6])
		{
			return 5;
		}
		else if (rgb < pallete[7])
		{
			return 6;
		}
		else
		{
			return 7;
		}
	}
}
