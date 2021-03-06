package me.naftoreiclag.fileparsecommons;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class ParseCommons
{
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};

	public static boolean[][] readCollisionArray(ByteBuffer buffer, int tWidth, int tHeight)
	{
		boolean[][] returnVal = new boolean[tWidth][tHeight];
		
		int ehx = 0;
		int why = 0;
		while(true)
		{
			byte eightTiles = buffer.get();
			
			for(int i = 0; i < 8; ++ i)
			{
				returnVal[ehx ++][why] = (eightTiles & (1 << i)) > 0;
				
				if(ehx >= tWidth)
				{
					ehx = 0;
					++ why;
					
					if(why >= tHeight)
					{
						break;
					}
				}
			}
			
			if(why >= tHeight)
			{
				break;
			}
		}
		
		return returnVal;
	}

	public static byte[][] readAlphaedArray(ByteBuffer data, int pWidth, int pHeight)
	{
		byte[][] returnVal = new byte[pWidth][pHeight];

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
				returnVal[ehx ++][why] = color;
	
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
		
		return returnVal;
	}
	
	public static byte[][] readUnalphaedArray(ByteBuffer data, int pWidth, int pHeight)
	{
		byte[][] ret = new byte[pWidth][pHeight];
		
		int ehx = 0;
		int why = 0;
		
		while(true)
		{
			byte colorStrip = data.get();
			
			byte color = (byte) (colorStrip & 0x07);
			int width = (colorStrip & 0xff) >> 3;
			
			for(int x = 0; x < width; ++ x)
			{
				ret[ehx ++][why] = color;
	
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
	
	public static void writeCollisionArray(boolean[][] collisionData, int tWidth, int tHeight, List<Byte> bites)
	{
		byte buildAByte = 0;
		int position = 0;
		
		for(int ty = 0; ty < tHeight; ++ ty)
		{
			for(int tx = 0; tx < tWidth; ++ tx)
			{
				if(collisionData[tx][ty])
				{
					buildAByte = (byte) (buildAByte | (1 << position));
				}
				
				++ position;
				
				if(position == 8 || (tx == tWidth - 1 && ty == tHeight - 1))
				{
					bites.add(buildAByte);
					buildAByte = 0;
					position = 0;
				}
			}
		}
	}

	// Appends an alphaed byte[][] to a Byte List
	public static void writeAlphaedByteArray(byte[][] pixelData, int pWidth, int pHeight, List<Byte> bites)
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

	// Appends an unalphaed byte[][] to a Byte List
	public static void writeUnalhaedByteArray(byte[][] pixelData, int pWidth, int pHeight, List<Byte> bites)
	{
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
	}
	
	// Returns an alphaed byte[][] from an Image
	public static byte[][] convertImageToAlphaedArray(BufferedImage image, int pWidth, int pHeight)
	{
		byte[][] pixelData = new byte[pWidth][pHeight];
	
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(x, y));
			}
		}
		
		return pixelData;
	}

	// Returns an unalphaed byte[][] from an Image
	public static byte[][] convertImageToUnalphaedArray(BufferedImage image, int pWidth, int pHeight)
	{
		byte[][] pixelData = new byte[pWidth][pHeight];
	
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(x, y));
			}
		}
		
		return pixelData;
	}

	public static BufferedImage convertEitherArrayToImage(byte[][] pixelData, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				byte color = pixelData[x][y];
				
				if(color < 8)
				{
					ret.setRGB(x, y, pallete[color] | 0xFF000000);
				}
				else
				{
					ret.setRGB(x, y, 0);
				}
			}
		}
		
		return ret;
	}

	public static BufferedImage readAlphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		return convertEitherArrayToImage(readAlphaedArray(data, pWidth, pHeight), pWidth, pHeight);
	}

	public static BufferedImage readUnalphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		return convertEitherArrayToImage(readUnalphaedArray(data, pWidth, pHeight), pWidth, pHeight);
	}

	private static byte getByteFromRGB(int rgb)
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
