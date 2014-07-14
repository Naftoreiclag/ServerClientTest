package me.naftoreiclag.clienttwo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import me.naftoreiclag.cliyent.Chunk;
import me.naftoreiclag.cliyent.Player;

public class NetworkingHandler
{
	public final DataOutputStream toServer;
	public final DataInputStream fromServer;
	
	public NetworkingHandler(DataOutputStream toServer, DataInputStream fromServer)
	{
		this.toServer = toServer;
		this.fromServer = fromServer;
	}
	
	/*
	public ByteBuffer getFile(byte type, long id) throws IOException
	{
		System.out.println("Requesting file");
		toServer.write(type);
		toServer.writeLong(id);
		System.out.println("Sent request for file: type: " + type + " id: " + id);
		
		int expectedFileSize = fromServer.readInt();
		System.out.println("File will be " + expectedFileSize + " bytes");
		
		if(expectedFileSize == 0)
		{
			return null;
		}

		byte[] chunkBytes = new byte[expectedFileSize];
	    int actualFileSize = fromServer.read(chunkBytes, 0, chunkBytes.length);
	    
	    if(actualFileSize != expectedFileSize)
	    {
	    	System.err.println("Warning! File received was " + actualFileSize + " bytes instead of " + expectedFileSize + "bytes!");
	    }
	    else
	    {
	    	System.out.println("File received sucessfully.");
	    }
	    
	    ByteBuffer data = ByteBuffer.wrap(chunkBytes);
		
		return data;
	}
	*/
	
	public Chunk getChunk(long id) throws IOException
	{
		System.out.println("Requesting chunk");
		toServer.write(1);
		toServer.writeLong(id);
		System.out.println("Sent request for chunk " + id);
		
		int expectedFileSize = fromServer.readInt();
		System.out.println("File will be " + expectedFileSize + " bytes");
		
		if(expectedFileSize == 0)
		{
			return null;
		}

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
	    
	    ByteBuffer data = ByteBuffer.wrap(chunkBytes);
		
		return new Chunk(data);
	}
	
	public Area getArea(long id)
	{
		
	}
}
