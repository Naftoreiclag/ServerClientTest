package me.naftoreiclag.test.copy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
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

	@Override
	public void save(File file) throws IOException
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) cWidth);
		bites.add((byte) cHeight);

		ParseCommons.writeCollisionArray(collisionData, tWidth, tHeight, bites);
		ParseCommons.writeAlphaedByteArray(pixelData, pWidth, pHeight, bites);
		
		FooIOUtil.writeListToFile(file, bites);
	}
	
	//////////////////////////////////////////////////////////////

	@Override
	public void draw(Graphics2D g2, int zoom)
	{
		g2.drawImage(displayImage, 0, 0, pWidth * zoom, pHeight * zoom, null);
		
		drawLines(g2, cWidth, cHeight, zoom);
		drawBoxes(g2, cWidth, cHeight, zoom);
	}
	
	private AlphaComposite makeAlphaComposite(float alpha)
	{
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
	
	public void drawBoxes(Graphics2D g2, int cWidth, int cHeight, int scale)
	{
		Composite originalComposite = g2.getComposite();

		g2.setComposite(makeAlphaComposite(0.2f));
		
		g2.setColor(Color.RED);
		
		for(int x = 0; x < tWidth; ++ x)
		{
			for(int y = 0; y < tHeight; ++ y)
			{
				if(collisionData[x][y])
				{
					g2.fillRect(x * 8 * scale, y * 8 * scale, 8 * scale, 8 * scale);
				}
			}
		}
		
		
		g2.setComposite(originalComposite);
	}
	
	public void drawLines(Graphics2D g2, int cWidth, int cHeight, int scale)
	{
		int ox = 0;
		int oy = 0;
		
		Composite originalComposite = g2.getComposite();
		
		g2.setComposite(makeAlphaComposite(0.2f));
		
		for(int cx = 0; cx < cWidth; ++ cx)
		{
			for(int cy = 0; cy < cHeight; ++ cy)
			{
				int px = ox + ((cx << 7) * scale);
				int py = oy + ((cy << 7) * scale);
				
				int wid = 128 * scale;

				g2.setColor(Color.BLUE);
				
				for(int sx = 1; sx < 16; ++ sx)
				{
					int ssx = (sx << 3) * scale;
					
					
					// horz lines
					g2.drawLine(px, py + ssx, px + wid, py + ssx);
					
					// vert lines
					g2.drawLine(px + ssx, py, px + ssx, py + wid);
				}
				g2.setColor(Color.RED);

				g2.drawRect(px, py, px + wid, py + wid);
			}
		}
		
		g2.setComposite(originalComposite);
	}
}
