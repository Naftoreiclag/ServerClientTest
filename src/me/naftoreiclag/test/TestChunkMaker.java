package me.naftoreiclag.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TestChunkMaker
{
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public static void main(String[] args) throws Exception
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
		
		//bites.add((byte) 128);
		//bites.add((byte) 128);
		
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
				
				if(color != pixelData[px][py] || size >= 31)
				{
					bites.add((byte) ((size << 3) + color));
					color = pixelData[px][py];
					size = 1;
					
					continue;
				}
				
				++ size;
			}
		}
		bites.add((byte) ((size << 3) + color));
		
		// Writing
		
		byte[] imgData = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			imgData[i] = bites.get(i);
		}
		
		int[] aaa = {
				 9, 10, 11, 12, 13, 
				16, 17, 18, 19, 20, 
				23, 24, 25, 26, 27, 
				30, 31, 32, 33, 34, 
				37, 38, 39, 40, 41};
		
		for(int id : aaa)
		{
			byte[] data = new byte[6 + 32 + imgData.length];
			
			int byteIndex = 0;
			
			data[byteIndex ++] = (byte) (id);
			data[byteIndex ++] = (byte) (id - 1);
			data[byteIndex ++] = (byte) (id - 7);
			data[byteIndex ++] = (byte) (id + 1);
			data[byteIndex ++] = (byte) (id + 7);
			data[byteIndex ++] = 0;
			
			for(int i = 0; i < 32; ++ i)
			{
				data[byteIndex ++] = 0;
			}
			
			for(byte b : imgData)
			{
				data[byteIndex ++] = b;
			}
			
			FileOutputStream fos;
			fos = new FileOutputStream("server/map/chunks/" + id + ".c");
			fos.write(data);
			fos.close();
		}
		
		/*
		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
		*/
	}
	
	/*
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
	 */
	

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
