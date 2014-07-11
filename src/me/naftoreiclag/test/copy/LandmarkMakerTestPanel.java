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
	
	Project project = null;

	int zoom = 4;

	private JPanel selectionPane;
	
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

		project.mMove(e);
		
		this.repaint();
	}
	
	private void mPress(MouseEvent e)
	{
		if(project == null)
		{
			return;
		}
		
		project.mPress(e);
		
		this.repaint();
	}
	private void mRelease(MouseEvent e)
	{
		if(project == null)
		{
			return;
		}
		
		project.mRelease(e);
		
		this.repaint();
	}

	public void onFileNew(ActionEvent e)
	{
		project = null;
		syncSelectionPane();
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
		ap.addLandmark(lp);
		
		syncSelectionPane();
	}
	
	public void syncSelectionPane()
	{
		selectionPane.removeAll();
		
		if(project instanceof AreaProject)
		{
			AreaProject ap = (AreaProject) project;
			
			for(int i = 0; i < ap.numLandmarks; ++ i)
			{
				JButton newButt = new JButton();
				newButt.setIcon(new ImageIcon(ap.landmarks.get(i).displayImage));
				newButt.addActionListener(new DumbButton(i));
				
				selectionPane.add(newButt);
			}
		}
		
		selectionPane.updateUI();
		return;
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
			project.selectedOne = id;
		}
	}
	public class FooButton implements ActionListener
	{
		int id;
		
		public FooButton(int i)
		{
			this.id = i;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			project.removeLandmark(id);
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

	public void setLandmarkPanel(JPanel foo)
	{
		this.selectionPane = foo;
	}
}
