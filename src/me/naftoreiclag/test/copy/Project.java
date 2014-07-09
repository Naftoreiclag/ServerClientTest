package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Project
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

	public Project(BufferedImage image)
	{
		tWidth = image.getWidth() >> 3;
		tHeight = image.getHeight() >> 3;
		pWidth = tWidth << 3;
		pHeight = tHeight << 3;
		collisionData = new boolean[tWidth][tHeight];
		
		pixelData = ParseCommons.convertImageToAlphaedArray(image, pWidth, pHeight);
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);
	}
	
	public Project(File file)
	{
		byte[] data = null;
		try
		{
			data = Files.readAllBytes(file.toPath());
		}
		catch (IOException e) { e.printStackTrace(); }
		
		System.out.println(data.length);
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		tWidth = buffer.get();
		tHeight = buffer.get();
		originX = buffer.get();
		originY = buffer.get();
		
		System.out.println(tWidth + " , " + tHeight);
		System.out.println(originX + " , " + originY);
		
		pWidth = tWidth << 3;
		pHeight = tHeight << 3;
		System.out.println(pWidth + " , " + pHeight);
		System.out.println(originX + " , " + originY);
		System.out.println("============");
		
		collisionData = ParseCommons.readCollisionArray(buffer, tWidth, tHeight);
		pixelData = ParseCommons.readAlphaedArray(buffer, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);

		System.out.println("loaded " + data.length);
	}

	public void save(File file) throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) tWidth);
		bites.add((byte) tHeight);
		bites.add((byte) originX);
		bites.add((byte) originY);

		ParseCommons.writeCollisionArray(collisionData, tWidth, tHeight, bites);
		ParseCommons.writeAlphaedByteArray(pixelData, pWidth, pHeight, bites);
		
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
		
		System.out.println("saved " + data.length);
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
