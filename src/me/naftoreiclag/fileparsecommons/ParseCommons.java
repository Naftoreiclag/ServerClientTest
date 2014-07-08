package me.naftoreiclag.fileparsecommons;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

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
	
	//====
	
	// Unalphaed Buffer -> Image
	// Alphaed Buffer -> Image
	// Image -> Alphaed byte[][]
	// Image -> Unalphaed byte[][]
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};

	public static byte[][] readAlphaedArray(ByteBuffer data, int pWidth, int pHeight)
	{
		byte[][] ret = new byte[pWidth][pHeight];

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
					ret[ehx ++][why] = 0;
				}
				else
				{
					ret[ehx ++][why] = color;
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
	
	// Returns an Image created from bytes read from an alphaed Buffer
	public static BufferedImage readAlphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		return arrayToImage(readAlphaedArray(data, pWidth, pHeight), pWidth, pHeight);
	}


	// Returns an Image created from bytes read from an unalphaed Buffer
	public static BufferedImage readUnalphaedImage(ByteBuffer data, int pWidth, int pHeight)
	{
		return arrayToImage(readUnalphaedArray(data, pWidth, pHeight), pWidth, pHeight);
	}
	
	public static BufferedImage arrayToImage(byte[][] pixelData, int pWidth, int pHeight)
	{
		BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < pWidth; ++x)
		{
			for(int y = 0; y < pHeight; ++y)
			{
				byte color = pixelData[x][y];
				
				if(color == 8)
				{
					ret.setRGB(x, y, 0);
				}
				else
				{
					ret.setRGB(x, y, pallete[color] | 0xFF000000);
				}
			}
		}
		
		return ret;
	}

	// Returns an alphaed byte[][] from an Image
	public static byte[][] imageToAlphaedArray(BufferedImage image, int pWidth, int pHeight)
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
	public static byte[][] imageToUnalphaedArray(BufferedImage image, int pWidth, int pHeight)
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
	
	public static boolean[][] readCollisionArray(ByteBuffer data2, int tWidth, int tHeight)
	{
		boolean[][] ret = new boolean[tWidth][tHeight];
		
		int colX = 0;
		int colY = 0;
		while(true)
		{
			byte eightTiles = data2.get();
			
			for(int i = 0; i < 8; ++ i)
			{
				ret[colX ++][colY] = (eightTiles & (1 << i)) > 0;
				
				if(colX >= 16)
				{
					colX = 0;
					++ colY;
					
					if(colY >= 16)
					{
						break;
					}
				}
			}
			
			if(colY >= 16)
			{
				break;
			}
		}
		
		return ret;
	}
	
	public static void writeCollisionArray(boolean[][] collisionData, int tWidth, int tHeight, List<Byte> bites)
	{
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
