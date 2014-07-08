package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;

public class LoadedLandmarkProject
{
	byte[][] pixelData;
	boolean[][] collisionData;
	
	int originX = 0;
	int originY = 0;

	BufferedImage displayImage;
	
	int tWidth;
	int tHeight;
	
	int pWidth;
	int pHeight;
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};

	public LoadedLandmarkProject(BufferedImage image)
	{
		tWidth = image.getWidth() >> 3;
		tHeight = image.getHeight() >> 3;
		collisionData = new boolean[tWidth][tHeight];
		System.out.println("New projected loaded with " + tWidth + " by " + tHeight);
		pWidth = tWidth << 3;
		pHeight = tHeight << 3;
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
				byte color = pixelData[x][y];

				if(color < 8)
				{
					displayImage.setRGB(x, y, pallete[color] | 0xFF000000);
				}
				else
				{
					displayImage.setRGB(x, y, 0);
				}
			}
		}
	}
	
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
