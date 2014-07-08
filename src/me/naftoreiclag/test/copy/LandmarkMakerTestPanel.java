package me.naftoreiclag.test.copy;

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
public class LandmarkMakerTestPanel extends JPanel
{
	JFileChooser fileChooser = new JFileChooser();
	
	JScrollPane scrollContainer = null;
	
	LoadedLandmarkProject lp = null;
	
	boolean leftDown = false;
	boolean rightDown = false;

	int zoomLevel = 2;
	JLabel picLabel;
	
	public LandmarkMakerTestPanel() throws Exception
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
			
			lp = new LoadedLandmarkProject(image);
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
		
		drawLines(g2, zoomLevel);
		drawBoxes(g2, zoomLevel);
	}
	
	private AlphaComposite makeAlphaComposite(float alpha)
	{
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
	
	public void drawBoxes(Graphics2D g2, int scale)
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
	
	public void drawLines(Graphics2D g2, int scale)
	{
		Composite originalComposite = g2.getComposite();

		g2.setComposite(makeAlphaComposite(0.2f));

		g2.setColor(Color.BLUE);
		for(int sx = 1; sx < lp.tWidth; ++sx)
		{
			int ssx = (sx << 3) * scale;

			// vert lines
			g2.drawLine(ssx, 0, ssx, lp.pWidth * scale);
		}

		for(int sy = 1; sy < lp.tHeight; ++sy)
		{

			int ssy = (sy << 3) * scale;

			// horz lines
			g2.drawLine(0, ssy, lp.pHeight * scale, ssy);
		}

		g2.setColor(Color.RED);

		g2.drawRect(0, 0, lp.pWidth * scale, lp.pHeight * scale);

		g2.setComposite(originalComposite);
	}

	public void giveScrolPaneAccess(JScrollPane scrollContainer)
	{
		this.scrollContainer = scrollContainer;
		
	}
}
