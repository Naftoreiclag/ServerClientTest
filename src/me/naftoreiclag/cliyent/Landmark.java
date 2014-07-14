package me.naftoreiclag.cliyent;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class Landmark
{
	protected final int tWidth;
	protected final int tHeight;
	
	protected final boolean[][] collision;
	
	protected final BufferedImage image;
	
	public Landmark(ByteBuffer data)
	{
		tWidth = data.get() & 0xFF;
		tHeight = data.get() & 0xFF;
		
		collision = ParseCommons.readCollisionArray(data, tWidth, tHeight);
		image = ParseCommons.readAlphaedImage(data, tWidth << 3, tHeight << 3);
	}
}
