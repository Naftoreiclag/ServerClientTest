package me.naftoreiclag.test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AreaMakerTestPanel extends JPanel
{
	JFileChooser fileChooser = new JFileChooser();
	
	JScrollPane scrollContainer = null;
	
	LoadedProject lp = null;
	
	boolean leftDown = false;
	boolean rightDown = false;

	int zoomLevel = 2;
	JLabel picLabel;
	
	public AreaMakerTestPanel() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		fileChooser.setCurrentDirectory(new File("C:/Life/Naftoreiclag/Programming/Git/ServerClientTest/tests"));
	
		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e){ mMove(e); }
			@Override
			public void mouseMoved(MouseEvent e){ mMove(e); }
		});
		
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) { kPress(e); }
			@Override
			public void keyReleased(KeyEvent e) { kRelease(e); }
			@Override
			public void keyTyped(KeyEvent e) { }
		});
		
		this.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { mPress(e); }
			@Override
			public void mouseReleased(MouseEvent e) { mRelease(e); }
		});
	}

	private void mMove(MouseEvent e)
	{
		if(lp == null)
		{
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		
		x /= zoomLevel;
		y /= zoomLevel;
		
		x = x >> 3;
		y = y >> 3;
		
		if(x >= lp.tWidth || y >= lp.tHeight)
		{
			System.out.println("ogaeroigjger" + x + ", " + y);
			return;
		}
		
		if(leftDown)
		{
			lp.collisionData[x][y] = true;
		}
		else if(rightDown)
		{
			lp.collisionData[x][y] = false;
		}
		
		this.repaint();
	}
	private void mPress(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftDown = true;
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
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightDown = false;
		}
	}
	private void kPress(KeyEvent e)
	{
	}
	private void kRelease(KeyEvent e)
	{
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

			this.setSize(lp.pWidth * 2, lp.pHeight * 2);
			/*
			if(scrollContainer != null)
			{
				scrollContainer.setPreferredSize(new Dimension(lp.pWidth, lp.pHeight));
			}
			*/
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
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setBackground(Color.WHITE);
		
		if(lp == null)
		{
			return;
		}
		
		BufferedImage img = lp.displayImage;
		
		g2.drawImage(img, 0, 0, img.getWidth() * zoomLevel, img.getHeight() * zoomLevel, null);
		
		drawLines(g2, lp.cWidth, lp.cHeight, zoomLevel);
		drawBoxes(g2, lp.cWidth, lp.cHeight, zoomLevel);
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
		
		for(int x = 0; x < lp.tWidth; ++ x)
		{
			for(int y = 0; y < lp.tHeight; ++ y)
			{
				if(lp.collisionData[x][y])
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

	public void giveScrolPaneAccess(JScrollPane scrollContainer)
	{
		this.scrollContainer = scrollContainer;
		
	}
}
