package me.naftoreiclag.cliyent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	public static class MainFrame extends JFrame
	{
		private MainFrame()
		{
			super("Client");
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(500, 500);
			this.setLocationRelativeTo(null);

			MainPanel m = new MainPanel();
			this.add(m);
		}
	}
	
	public static void main(String argv[]) throws Exception
	{
		MainFrame m = new MainFrame();
		m.setVisible(true);
	}
	
	public MainPanel()
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		(new Thread()
		{
			double lastTick = System.currentTimeMillis();
			
			@Override
			public void run()
			{
				while(true)
				{
					if(System.currentTimeMillis() > lastTick + 10d)
					{
						lastTick = System.currentTimeMillis();
						
						
						
						repaint();
					}
				}
		    }
		}).start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		
		
	}
}
