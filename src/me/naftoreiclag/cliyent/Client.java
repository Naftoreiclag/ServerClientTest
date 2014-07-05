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
	
	public Client() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		map = new LocalMap();
		player = new Player();
		
		Socket socket = new Socket(address, port);
		DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		DataInputStream fromServer = new DataInputStream(socket.getInputStream());
		System.out.println("Connection established to " + address + ":" + port + "...");
		
		System.out.println("Waiting for server to give a spawn chunk");
		long spawnChunk = fromServer.readLong();
		
		(new Thread()
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
		}).start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		
		map.paint(g2, player);
	}
	
	public static void maain(String argv[]) throws Exception
	{
		/*
		String address = "localhost";
		int port = 1337;

		Socket socket = new Socket(address, port);
		DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		DataInputStream fromServer = new DataInputStream(socket.getInputStream());
		System.out.println("Connection established to " + address + ":" + port + "...");
		
		System.out.println("Waiting for server to give a spawn chunk");
		long spawnChunk = fromServer.readLong();
		
		toServer.writeByte(1);
		
		System.out.println("Requesting chunk");
		toServer.writeLong(spawnChunk);
		System.out.println("Sent request for chunk " + spawnChunk);
		
		int expectedFileSize = fromServer.readInt();
		System.out.println("File will be " + expectedFileSize + " bytes");

		byte[] chunkBytes = new byte[expectedFileSize];
	    int actualFileSize = fromServer.read(chunkBytes, 0, chunkBytes.length);
	    
	    if(actualFileSize != expectedFileSize)
	    {
	    	System.err.println("Warning! Chunk received was " + actualFileSize + "bytes instead of " + expectedFileSize + "bytes!");
	    }
	    else
	    {
	    	System.out.println("Chunk received sucessfully.");
	    }

	    BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream("client/map/chunks" + spawnChunk + ".chunk"));
	    toFile.write(chunkBytes, 0, actualFileSize);
	    toFile.close();
    	System.out.println("Written to " + "client/map/chunks" + spawnChunk + ".chunk" + ".");
    	
	    socket.close();
		System.out.println("Socket closed.");
		*/

	}
}
