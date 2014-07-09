package me.naftoreiclag.test.copy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
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

	@Override
	public void write(List<Byte> bites)
	{
		bites.add((byte) tWidth);
		bites.add((byte) tHeight);
		bites.add((byte) originX);
		bites.add((byte) originY);
		ParseCommons.writeCollisionArray(collisionData, tWidth, tHeight, bites);
		ParseCommons.writeAlphaedByteArray(pixelData, pWidth, pHeight, bites);
	}
	
	//////////////////////////////////////////////////////////////
	
	@Override
	public void draw(Graphics2D g2, int zoom)
	{
		drawStuff(g2, zoom);

		g2.drawImage(displayImage, 0, 0, pWidth * zoom, pHeight * zoom, null);

		drawThings(g2, zoom);

		drawLines(g2, zoom);
		drawBoxes(g2, zoom);
	}

	private AlphaComposite makeAlphaComposite(float alpha)
	{
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}

	public void drawStuff(Graphics2D g2, int zoom)
	{

		g2.setColor(Color.YELLOW);
		g2.fillRect(0, 0, pWidth * zoom, pHeight * zoom);
	}

	public void drawThings(Graphics2D g2, int zoom)
	{
		g2.setColor(Color.RED);
		g2.drawRect(originX * 8 * zoom, originY * 8 * zoom, 8 * zoom, 8 * zoom);
	}

	public void drawBoxes(Graphics2D g2, int zoom)
	{
		Composite originalComposite = g2.getComposite();

		g2.setComposite(makeAlphaComposite(0.2f));

		g2.setColor(Color.RED);

		for (int x = 0; x < tWidth; ++x)
		{
			for (int y = 0; y < tHeight; ++y)
			{
				if (collisionData[x][y])
				{
					g2.fillRect(x * 8 * zoom, y * 8 * zoom, 8 * zoom, 8 * zoom);
				}
			}
		}

		g2.setComposite(originalComposite);
	}

	public void drawLines(Graphics2D g2, int zoom)
	{
		Composite originalComposite = g2.getComposite();

		g2.setComposite(makeAlphaComposite(0.2f));

		g2.setColor(Color.BLUE);
		for (int sx = 1; sx < tWidth; ++sx)
		{
			int ssx = (sx << 3) * zoom;

			// vert lines
			g2.drawLine(ssx, 0, ssx, pHeight * zoom);
		}

		for (int sy = 1; sy < tHeight; ++sy)
		{

			int ssy = (sy << 3) * zoom;

			// horz lines
			g2.drawLine(0, ssy, pWidth * zoom, ssy);
		}

		g2.setColor(Color.RED);

		g2.drawRect(0, 0, pWidth * zoom, pHeight * zoom);

		g2.setComposite(originalComposite);
	}
}
