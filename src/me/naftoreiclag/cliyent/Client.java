package me.naftoreiclag.cliyent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Client extends JFrame
{
	private Client()
	{
		super("Pig Collision Demo");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);

		MainPanel m = new MainPanel();
		this.add(m);
	}
	
	public static void maain(String argv[]) throws Exception
	{
		Client c = new Client();
		c.setVisible(true);
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
