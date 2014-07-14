package me.naftoreiclag.clienttwo;

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
	
	public Client() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		
		socket = new Socket(address, port);
		toServer = new DataOutputStream(socket.getOutputStream());
		fromServer = new DataInputStream(socket.getInputStream());
		System.out.println("Connection established to " + address + ":" + port + "...");
		
		System.out.println("Waiting for server to give a spawn chunk");
		long spawnChunk = fromServer.readLong();
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
	}
}
