package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class LandmarkProject extends Project
{
	byte[][] pixelData;
	boolean[][] collisionData;
	
	int originX;
	int originY;

	BufferedImage displayImage;
	
	int tWidth;
	int tHeight;
	
	int pWidth;
	int pHeight;
	
	public LandmarkProject(BufferedImage image)
	{
		super(image);
		
		tWidth = image.getWidth() >> 3;
		tHeight = image.getHeight() >> 3;
		originX = 0;
		originY = 0;
		pWidth = tWidth << 3;
		pHeight = tHeight << 3;
		
		collisionData = new boolean[tWidth][tHeight];
		
		pixelData = ParseCommons.convertImageToAlphaedArray(image, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);
	}
	
	public LandmarkProject(ByteBuffer buffer)
	{
		super(buffer);
		
		tWidth = buffer.get();
		tHeight = buffer.get();
		originX = buffer.get();
		originY = buffer.get();
		pWidth = tWidth << 3;
		pHeight = tHeight << 3;
		
		collisionData = ParseCommons.readCollisionArray(buffer, tWidth, tHeight);
		
		pixelData = ParseCommons.readAlphaedArray(buffer, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);
	}

	public void save(File file) throws IOException
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
}
