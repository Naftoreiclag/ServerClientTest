package me.naftoreiclag.cliyent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Client extends JPanel
{
	public static class MainFrame extends JFrame
	{
		private MainFrame() throws Exception
		{
			super("Client");
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(500, 500);
			this.setLocationRelativeTo(null);

			Client client = new Client();
			this.add(client);
		}
	}
	
	public static void main(String argv[]) throws Exception
	{
		MainFrame m = new MainFrame();
		m.setVisible(true);
	}
	
	public LocalMap map;
	public Player player;
	
	String address = "localhost";
	int port = 1337;
	
	Socket socket;
	DataOutputStream toServer;
	DataInputStream fromServer;
	
	public Client() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		
		socket = new Socket(address, port);
		toServer = new DataOutputStream(socket.getOutputStream());
		fromServer = new DataInputStream(socket.getInputStream());
		map = new LocalMap(toServer, fromServer);
		System.out.println("Connection established to " + address + ":" + port + "...");
		
		System.out.println("Waiting for server to give a spawn chunk");
		long spawnChunk = fromServer.readLong();

		player = new Player(spawnChunk);
		
		new Thread()
		{
			double lastTick = System.currentTimeMillis();
			
			@Override
			public void run()
			{
				while(true)
				{
					double rightNow = System.currentTimeMillis();
					if(rightNow > lastTick + 10d)
					{
						double delta = rightNow - lastTick;
						lastTick = rightNow;
						
						repaint();
					}
				}
		    }
		}.start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		
		map.paint(g2, player);
	}
}
