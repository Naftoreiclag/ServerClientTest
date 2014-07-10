package me.naftoreiclag.test.copy;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public abstract class Project
{
	int tWidth;
	int tHeight;
	
	int pWidth;
	int pHeight;
	
	boolean leftDown = false;
	boolean middleDown = false;
	boolean rightDown = false;

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

	public abstract void write(List<Byte> bites);

	public void draw(Graphics2D g2, int zoom)
	{
	}
	
	private void mMove(MouseEvent e)
	{
	}
	
	private void mPress(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftDown = true;
		}
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			middleDown = true;
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightDown = true;
		}
	}
	
	private void mRelease(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftDown = false;
		}
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			middleDown = false;
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightDown = false;
		}
	}
}
