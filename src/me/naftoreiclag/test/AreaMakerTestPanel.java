package me.naftoreiclag.test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AreaMakerTestPanel extends JPanel
{
	JFileChooser fileChooser = new JFileChooser();
	
	LoadedProject lp = null;
	
	public AreaMakerTestPanel() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		fileChooser.setCurrentDirectory(new File("C:/Life/Naftoreiclag/Programming/Git/ServerClientTest/tests"));
	}

	public void onFileNew(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void onFileCFIPressed(ActionEvent e)
	{
		int returnVal = fileChooser.showOpenDialog(this);

		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			
			BufferedImage image = null;
			
			try
			{
				image = ImageIO.read(file);
			}
			catch (IOException e2)
			{
				e2.printStackTrace();
			}
			
			lp = new LoadedProject(image);
			
		}
		else
		{
			System.out.println("closed");
		}
		this.repaint();
	}

	public void onFileOpen(ActionEvent e)
	{
		
	}

	public void onFileSave(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		int zoomLevel = 2;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setBackground(Color.WHITE);
		
		if(lp == null)
		{
			return;
		}
		
		BufferedImage img = lp.displayImage;
		
		g2.drawImage(img, 0, 0, img.getWidth() * zoomLevel, img.getHeight() * zoomLevel, null);
		
		drawLines(g2, lp.cWidth, lp.cHeight, zoomLevel);
	}
	
	private AlphaComposite makeAlphaComposite(float alpha)
	{
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
	
	public void drawLines(Graphics2D g2, int cWidth, int cHeight, int scale)
	{
		int ox = 0;
		int oy = 0;
		
		Composite originalComposite = g2.getComposite();
		
		g2.setComposite(makeAlphaComposite(0.1f));
		
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

				// top
				g2.drawLine(px, py, px + wid, py);
				
				// bot
				g2.drawLine(px, py + wid, px + wid, py + wid);

				// lef
				g2.drawLine(px, py, px, py + wid);
				
				// rit
				g2.drawLine(px + wid, py, px + wid, py + wid);
			}
		}
		
		g2.setComposite(originalComposite);
	}
}
