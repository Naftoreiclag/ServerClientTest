package me.naftoreiclag.clienttwo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
			super("Client two");
			
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
	
	String address = "localhost";
	int port = 1337;
	
	Socket socket;
	DataOutputStream toServer;
	DataInputStream fromServer;
	
	Player player;
	
	ChunkPool pool;
	
	public Client() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		player = new Player();
		
		socket = new Socket(address, port);
		toServer = new DataOutputStream(socket.getOutputStream());
		fromServer = new DataInputStream(socket.getInputStream());
		System.out.println("Connection established to " + address + ":" + port + "...");
		
		System.out.println("Waiting for server to give a spawn chunk");
		updatePosition();
		
		pool = new ChunkPool(toServer, fromServer);
		
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) { kPress(e); }
			@Override
			public void keyReleased(KeyEvent e) { kRelease(e); }
			@Override
			public void keyTyped(KeyEvent e) { }
		});
	}
	
	private void kPress(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		try
		{
			if(key == 37)
			{
				goWest();
			}
			else if(key == 39)
			{
				goEast();
			}
			else if(key == 38)
			{
				goNorth();
			}
			else if(key == 40)
			{
				goSouth();
			}
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
		
		this.repaint();
	}
	
	private void kRelease(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void updatePosition() throws IOException
	{
		fromServer.readByte();
		
		player.cId = fromServer.readLong();
		player.x = fromServer.readByte();
		player.y = fromServer.readByte();
	}
	
	private void goWest() throws IOException
	{
		toServer.write(0x02);
		updatePosition();
	}
	private void goNorth() throws IOException
	{
		toServer.write(0x03);
		updatePosition();
	}
	private void goEast() throws IOException
	{
		toServer.write(0x04);
		updatePosition();
	}
	private void goSouth() throws IOException
	{
		toServer.write(0x05);
		updatePosition();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		
		pool.paint(g2, player);
	}
}
