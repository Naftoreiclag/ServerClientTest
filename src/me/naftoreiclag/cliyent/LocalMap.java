package me.naftoreiclag.cliyent;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalMap
{
	public Map<Long, Area> areas = new HashMap<Long, Area>();
	public Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();

	DataOutputStream toServer;
	DataInputStream fromServer;
	
	public LocalMap(DataOutputStream toServer, DataInputStream fromServer)
	{
		this.toServer = toServer;
		this.fromServer = fromServer;
	}

	public void paint(Graphics2D g2, Player player)
	{
		int viewRad = 2;
		int scale = 64;
		
		int offset = 0;
		
		Chunk northWest = getChunk(player.locationChunkID);
		for(int _ = 0; _ < viewRad; ++ _)
		{
			northWest = getChunk(northWest.nId);
			northWest = getChunk(northWest.wId);
		}
		
		// Pointer that stays in the west-most column and moves south with y
		Chunk pinpointA = northWest;
		for(int y = 0; y <= viewRad * 2; ++ y)
		{
			// Pointer that is the same row as A but moves east with x
			Chunk pinpointB = pinpointA;
			for(int x = 0; x <= viewRad * 2; ++ x)
			{
				g2.drawImage(pinpointB.image, x * scale, y * scale, scale, scale, null);
				
				// Move pointer B east, unless this is the last in the columns, in which case don't do anything because that's pointless.
				if(x != viewRad * 2) { pinpointB = getChunk(pinpointB.eId); }
			}

			// Move pointer A south, unless this is the last in the rows, in which case don't do anything because that's pointless.
			if(y != viewRad * 2) { pinpointA = getChunk(pinpointA.sId); }
		}
	}
	
	public Chunk getChunk(long id)
	{
		Chunk chunk = chunks.get(id);
		
		if(chunk == null)
		{
			try
			{
				chunk = getChunkFromServer(id);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(chunk == null)
			{
				System.err.println("Could not get chunk" + id + " from server!");
			}
			
			chunks.put(id, chunk);
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
		
		return new Chunk(chunkBytes);
	}
}
