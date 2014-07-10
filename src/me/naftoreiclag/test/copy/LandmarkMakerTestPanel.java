package me.naftoreiclag.test.copy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class LandmarkMakerTestPanel extends JPanel
{
	JFileChooser fileChooser = new JFileChooser();
	
	JScrollPane scrollContainer = null;
	
	Project project = null;
	
	boolean leftDown = false;
	boolean rightDown = false;

	int zoom = 4;
	JLabel picLabel;

	private JPanel objectPane;
	
	//public Map<JButton, Integer> buttons = new HashMap<JButton, Integer>();
	
	int selectedOne = 0;
	
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
		if(project == null)
		{
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		
		x /= zoom;
		y /= zoom;
		
		x = x >> 3;
		y = y >> 3;

		if (x >= project.tWidth || y >= project.tHeight || x < 0 || y < 0)
		{
			return;
		}
		
		if(leftDown)
		{
			project.collisionData[x][y] = true;
		}
		else if(rightDown)
		{
			project.collisionData[x][y] = false;
		}
		
		this.repaint();
	}
	
	private void mPress(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();

		x /= zoom;
		y /= zoom;

		x = x >> 3;
		y = y >> 3;
		
		if (x >= project.tWidth || y >= project.tHeight || x < 0 || y < 0)
		{
			return;
		}
		
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			leftDown = true;
		}
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			if(project == null) { return; }
			
			if(project instanceof LandmarkProject)
			{
				LandmarkProject lp = (LandmarkProject) project;

				lp.originX = x;
				lp.originY = y;
				
			}
			
			if(project instanceof AreaProject)
			{
				AreaProject ap = (AreaProject) project;

				ap.placeLandmark(selectedOne, x, y);
				
			}
			
			this.repaint();

		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			rightDown = true;
			
			if(project instanceof AreaProject)
			{
				AreaProject ap = (AreaProject) project;

				ap.placeLandmark(-1, x, y);
			}
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
		project = null;
		this.repaint();
	}

	public void onEditAddLandmarkPressed(ActionEvent e)
	{
		if(!(project instanceof AreaProject))
		{
			return;
		}
		
		int returnVal = fileChooser.showOpenDialog(this);
		
		LandmarkProject lp = null;
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			
			if(file.getName().endsWith(".landmark"))
			{
				lp = new LandmarkProject(FooIOUtil.readBufferFromFile(file));
			}
		}
		else
		{
			System.out.println("closed");
		}
		this.repaint();
		
		AreaProject ap = (AreaProject) project;
		
		JButton newButt = new JButton();
		newButt.setIcon(new ImageIcon(lp.displayImage));
		newButt.addActionListener(new DumbButton(ap.numLandmarks));
		
		objectPane.add(newButt);
		
		ap.addLandmark(lp);
		
		objectPane.repaint();
	}
	
	public class DumbButton implements ActionListener
	{
		int id;
		
		public DumbButton(int i)
		{
			this.id = i;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectedOne = id;
			
			System.out.println(selectedOne);
			
		}
	}

	public void onFileOpen(ActionEvent e)
	{
		int returnVal = fileChooser.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			
			if(file.getName().endsWith(".landmark"))
			{
				project = new LandmarkProject(FooIOUtil.readBufferFromFile(file));
			}
			else if(file.getName().endsWith(".landmark.png"))
			{
				project = new LandmarkProject(FooIOUtil.readImageFromFile(file));
			}
			else if(file.getName().endsWith(".area"))
			{
				project = new AreaProject(FooIOUtil.readBufferFromFile(file));
			}
			else if(file.getName().endsWith(".area.png"))
			{
				project = new AreaProject(FooIOUtil.readImageFromFile(file));
			}
			
			System.out.println(file.getName());
					
			this.setSize(project.pWidth * zoom, project.pHeight * zoom);
		}
		else
		{
			System.out.println("closed");
		}
		this.repaint();
	}

	public void onFileSave(ActionEvent e)
	{
		int returnVal = fileChooser.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try
			{
				List<Byte> bites = new ArrayList<Byte>();
				project.write(bites);
				FooIOUtil.writeListToFile(file, bites);
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			System.out.println("closed");
		}
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setBackground(Color.WHITE);
		
		if(project == null)
		{
			return;
		}

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, project.pWidth * zoom * 5, project.pHeight * zoom * 5);
		
		project.draw(g2, zoom);
	}

	public void giveScrolPaneAccess(JScrollPane scrollContainer)
	{
		this.scrollContainer = scrollContainer;
		
	}

	public void setLandmarkPanel(JPanel foo)
	{
		this.objectPane = foo;
	}

	
	
}
