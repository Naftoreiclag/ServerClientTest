package me.naftoreiclag.test.copy;

import java.util.List;

public class WritingUtil
{
	public static void writeAlhaedImage(byte[][] pixelData, int pWidth, int pHeight, List<Byte> bites)
	{
		byte color = pixelData[0][0];
		int size = 1;
		
		for(int py = 0; py < pHeight; ++ py)
		{
			for(int px = 0; px < pWidth; ++ px)
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
	}
}
