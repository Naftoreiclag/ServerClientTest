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

public class AreaProject extends Project
{
	int cWidth;
	int cHeight;
	
	public AreaProject(BufferedImage image)
	{
		super(image);
		
		cWidth = image.getWidth() >> 7;
		cHeight = image.getHeight() >> 7;
		tWidth = cWidth << 4;
		tHeight = cHeight << 4;
		pWidth = cWidth << 7;
		pHeight = cHeight << 7;
		
		collisionData = new boolean[tWidth][tHeight];
		
		pixelData = ParseCommons.convertImageToUnalphaedArray(image, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);
	}
	
	public AreaProject(ByteBuffer buffer)
	{
		super(buffer);
		
		cWidth = buffer.get();
		cHeight = buffer.get();
		tWidth = cWidth << 4;
		tHeight = cHeight << 4;
		pWidth = cWidth << 7;
		pHeight = cHeight << 7;
		
		collisionData = ParseCommons.readCollisionArray(buffer, tWidth, tHeight);
		
		pixelData = ParseCommons.readUnalphaedArray(buffer, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);
	}

	public void save(File file) throws IOException
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) cWidth);
		bites.add((byte) cHeight);

		ParseCommons.writeCollisionArray(collisionData, tWidth, tHeight, bites);
		ParseCommons.writeAlphaedByteArray(pixelData, pWidth, pHeight, bites);
		
		// Writing
		FooIOUtil.writeListToFile(file, bites);
	}
}
