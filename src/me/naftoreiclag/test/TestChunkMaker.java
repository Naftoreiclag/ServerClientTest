package me.naftoreiclag.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import naftoreiclag.twobitdesigner.Landmark;

public class TestChunkMaker
{
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public static void main(String[] args)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File("tests/horse.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		byte[][] pixelData = new byte[128][128];
		
		//
		
		for(int x = 0; x < 128; ++ x)
		{
			for(int y = 0; y < 128; ++ y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(x, y));
			}
		}
		
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) 128);
		bites.add((byte) 128);
		
		// Color Data
		
		byte color = pixelData[0][0];
		int size = 1;
		
		for(int py = 0; py < 128; ++ py)
		{
			for(int px = 0; px < 128; ++ px)
			{
				if(px == 0 && py == 0)
				{
					continue;
				}
				
				if(color == 8)
				{
					if(color != pixelData[px][py] || size >= 127)
					{
						bites.add((byte) (size | 0x80));
						color = pixelData[px][py];
						size = 1;
						
						continue;
					}
				}
				else
				{
					if(color != pixelData[px][py] || size >= 15)
					{
						bites.add((byte) ((size << 3) + color));
						color = pixelData[px][py];
						size = 1;
						
						continue;
					}
				}
				
				++ size;
			}
		}
		if(color == 8)
		{
			bites.add((byte) (size | 0x80));
		}
		else
		{
			bites.add((byte) ((size << 3) + color));
		}
		
		// Writing

		/*
		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
		*/
	}
	

	public static byte getByteFromRGB(int rgb)
	{
		if((rgb & 0xFF000000) == 0)
		{
			return 8;
		}
		
		rgb = rgb & 0x00FFFFFF;
		
		if(rgb < pallete[1])
		{
			return 0;
		}
		else if(rgb < pallete[2])
		{
			return 1;
		}
		else if(rgb < pallete[3])
		{
			return 2;
		}
		else if(rgb < pallete[4])
		{
			return 3;
		}
		else if(rgb < pallete[5])
		{
			return 4;
		}
		else if(rgb < pallete[6])
		{
			return 5;
		}
		else if(rgb < pallete[7])
		{
			return 6;
		}
		else
		{
			return 7;
		}
	}
}
