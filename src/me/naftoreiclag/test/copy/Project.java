package me.naftoreiclag.test.copy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Project
{
	int tWidth;
	int tHeight;
	
	int pWidth;
	int pHeight;

	BufferedImage displayImage;
	
	byte[][] pixelData;
	boolean[][] collisionData;
	
	public Project(BufferedImage image)
	{
		System.out.println("Loaded project from image");
	}
	
	public Project(ByteBuffer buffer)
	{
		System.out.println("Loaded project from buffer");
	}

	public abstract void save(File file) throws IOException;

	public void draw(Graphics2D g2, int zoom)
	{
	}
}
