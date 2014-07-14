package me.naftoreiclag.cliyent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ChunkPool
{
	public static List<Chunk> chunks = new ArrayList<Chunk>();
	
	public static DataOutputStream toServer;
	public static DataInputStream fromServer;
	
	public static void loadOriginChunk(long id) throws Exception
	{
		updateChunkFromServer(id, 0, 0);
	}
	
	public static void addChunk(Chunk c)
	{
		for(int i = 0; i < chunks.size(); ++ i)
		{
			Chunk d = chunks.get(i);
			
			if(d.localX == c.localX && d.localY == c.localY)
			{
				chunks.remove(i);
			}
		}
		
		chunks.add(c);
	}
	
	public static Chunk getChunk(int localX, int localY) throws Exception
	{
		for(int i = 0; i < chunks.size(); ++ i)
		{
			Chunk d = chunks.get(i);
			
			if(d.localX == localX && d.localY == localY)
			{
				return d;
			}
		}
		
		return updateChunkFromServer(localX, localY);
	}
	

	public static Chunk updateChunkFromServer(int localX, int localY) throws Exception
	{
		long requestId = -1;
		
		for(int i = 0; i < chunks.size(); ++ i)
		{
			Chunk d = chunks.get(i);
			
			if(d.localX - 1 == localX && d.localY == localY)
			{
				requestId = d.wId;
			}
			else if(d.localX == localX && d.localY - 1 == localY)
			{
				requestId = d.nId;
			}
			else if(d.localX + 1 == localX && d.localY == localY)
			{
				requestId = d.eId;
			}
			else if(d.localX == localX && d.localY + 1 == localY)
			{
				requestId = d.sId;
			}
		}
		
		if(requestId == -1)
		{
			System.out.println("geragewtjyrhtgejy");
			return new Chunk(localX, localY);
		}
		
		return updateChunkFromServer(requestId, localX, localY);
	}
	
	public static Chunk updateChunkFromServer(long requestId, int localX, int localY) throws Exception
	{
		System.out.println("Requesting chunk");
		toServer.write(1);
		toServer.writeLong(requestId);
		System.out.println("Sent request for chunk " + requestId);
		
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
		
		return new Chunk(localX, localY, data);
	}
}
