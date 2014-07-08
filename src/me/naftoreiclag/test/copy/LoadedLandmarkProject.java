package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
	
	public void save(File file) throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) tWidth);
		bites.add((byte) tHeight);
		bites.add((byte) originX);
		bites.add((byte) originY);

		// Collision Data
		
		int position = 0;
		byte buildAByte = 0;
		for(int ty = 0; ty < tHeight; ++ ty)
		{
			for(int tx = 0; tx < tWidth; ++ tx)
			{
				if(collisionData[tx][ty])
				{
					buildAByte = (byte) (buildAByte | (1 << position));
				}
				
				++ position;
				
				if(position == 8)
				{
					bites.add(buildAByte);
					
					position = 0;
				}
			}
		}
		
		// Color Data
		WritingUtil.writeAlhaedImage(pixelData, pWidth, pHeight, bites);
		
		// Writing
		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
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
