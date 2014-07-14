package me.naftoreiclag.cliyent;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class LocalServerFiles
{
	public Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();
	public Map<Long, Area> areas = new HashMap<Long, Area>();
	
	public getChunk(long id)
	{
		Chunk chunk = chunks.get(id);
		
		if(chunk == null)
		{
		}
		
		return chunk;
	}
	
	public Chunk getChunkFromServer(long id) throws Exception
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
}
