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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.naftoreiclag.cliyent.Landmark;
import me.naftoreiclag.fileparsecommons.ParseCommons;

public class AreaProject extends Project
{
	Map<Integer, LandmarkProject> landmarks = new HashMap<Integer, LandmarkProject>();
	
	int numLandmarks;
	int numLandmarkInstances;
	
	int cWidth;
	int cHeight;

	int[][] landmarkData;
	boolean[][] compositeCollisionData;
	
	public AreaProject(BufferedImage image)
	{
		super(image);
		
		cWidth = image.getWidth() >> 7;
		cHeight = image.getHeight() >> 7;
		tWidth = cWidth << 4;
		tHeight = cHeight << 4;
		pWidth = cWidth << 7;
		pHeight = cHeight << 7;
		numLandmarks = 0;
		numLandmarkInstances = 0;
		collisionData = new boolean[tWidth][tHeight];
		pixelData = ParseCommons.convertImageToUnalphaedArray(image, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);

		compositeCollisionData = new boolean[tWidth][tHeight];
		
		landmarkData = new int[tWidth][tHeight];
		for(int x = 0; x < tWidth; ++ x)
		{
			for(int y = 0; y < tHeight; ++ y)
			{
				landmarkData[x][y] = -1;
			}
		}
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
		numLandmarks = buffer.get();
		numLandmarkInstances = buffer.get();
		collisionData = ParseCommons.readCollisionArray(buffer, tWidth, tHeight);
		pixelData = ParseCommons.readUnalphaedArray(buffer, pWidth, pHeight);
		
		displayImage = ParseCommons.convertEitherArrayToImage(pixelData, pWidth, pHeight);

		compositeCollisionData = new boolean[tWidth][tHeight];
		
		for(int i = 0; i < numLandmarks; ++ i)
		{
			landmarks.put(i, new LandmarkProject(buffer));
		}
		
		landmarkData = new int[tWidth][tHeight];
		for(int x = 0; x < tWidth; ++ x)
		{
			for(int y = 0; y < tHeight; ++ y)
			{
				landmarkData[x][y] = -1;
			}
		}

		for(int i = 0; i < numLandmarkInstances; ++ i)
		{
			landmarkData[buffer.get()][buffer.get()] = buffer.get();
		}
	}
	
	public int addLandmark(LandmarkProject lp)
	{
		landmarks.put(numLandmarks, lp);
		return numLandmarks ++;
	}
	
	public void placeLandmark(int id, int x, int y)
	{
		landmarkData[x][y] = id;
		
		updateCompositeCollision();
	}

	private void updateCompositeCollision()
	{
		for(int y2 = 0; y2 < tHeight; ++ y2)
		{
			for(int x2 = 0; x2 < tWidth; ++ x2)
			{
				int id = landmarkData[x2][y2];
				
				if(id != -1)
				{
					LandmarkProject l = landmarks.get(id);
					
					for(int x = 0; x < l.tWidth; ++ x)
					{
						for(int y = 0; y < l.tHeight; ++ y)
						{
							if(l.collisionData[x][y])
							{
								int x1 = x2 + x - l.originX;
								
								if(x1 < 0 || x1 > tWidth)
								{
									continue;
								}
								
								int y1 = y2 + y - l.originY;
								
								if(y1 < 0 || y1 > tHeight)
								{
									continue;
								}
								
								compositeCollisionData[x1][y1] = true;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void write(List<Byte> bites)
	{
		bites.add((byte) cWidth);
		bites.add((byte) cHeight);
		bites.add((byte) numLandmarks);
		
		for(int y = 0; y < tHeight; ++ y)
		{
			for(int x = 0; x < tWidth; ++ x)
			{
				int id = landmarkData[x][y];
				
				if(id != -1)
				{
					++ numLandmarkInstances;
				}
			}
		}
		
		bites.add((byte) numLandmarkInstances);
		ParseCommons.writeCollisionArray(collisionData, tWidth, tHeight, bites);
		ParseCommons.writeAlphaedByteArray(pixelData, pWidth, pHeight, bites);
		
		for(int i = 0; i < numLandmarks; ++ i)
		{
			landmarks.get(i).write(bites);
		}
		
		for(int y = 0; y < tHeight; ++ y)
		{
			for(int x = 0; x < tWidth; ++ x)
			{
				int id = landmarkData[x][y];
				
				if(id != -1)
				{
					bites.add((byte) x);
					bites.add((byte) y);
					bites.add((byte) id);
				}
			}
		}
	}
	
	//////////////////////////////////////////////////////////////

	@Override
	public void draw(Graphics2D g2, int zoom)
	{
		g2.drawImage(displayImage, 0, 0, pWidth * zoom, pHeight * zoom, null);
		
		for(int y = 0; y < tHeight; ++ y)
		{
			for(int x = 0; x < tWidth; ++ x)
			{
				int id = landmarkData[x][y];
				
				if(id != -1)
				{
					LandmarkProject lp = landmarks.get(id);
					
					g2.drawImage(lp.displayImage, (x - lp.originX) * 8 *zoom, (y - lp.originY) * 8 * zoom, lp.pWidth * zoom, lp.pHeight * zoom, null);
				}
			}
		}
		
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
		g2.setColor(Color.YELLOW);
		
		for(int x = 0; x < tWidth; ++ x)
		{
			for(int y = 0; y < tHeight; ++ y)
			{
				if(compositeCollisionData[x][y])
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
