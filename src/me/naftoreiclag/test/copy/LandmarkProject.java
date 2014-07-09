package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class LandmarkProject extends Project
{
	int originX;
	int originY;
	
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

		FooIOUtil.writeListToFile(file, bites);
	}
}
