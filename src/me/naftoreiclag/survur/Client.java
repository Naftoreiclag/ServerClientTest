package me.naftoreiclag.survur;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class Client
{
	public static void main(String argv[]) throws Exception
	{
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

	}
}
