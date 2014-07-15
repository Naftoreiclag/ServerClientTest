package me.naftoreiclag.clienttwo;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkPool
{
	public Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();
	public List<Long> nonexistentChunks = new ArrayList<Long>();
	//public static Map<Long, Area> areas = new HashMap<Long, Area>();

	public final String serverDirectory = "server/";

	public final DataOutputStream toServer;
	public final DataInputStream fromServer;
	
	public ChunkPool(DataOutputStream toServer, DataInputStream fromServer)
	{
		this.toServer = toServer;
		this.fromServer = fromServer;
	}
	
	public Chunk getChunk(long id)
	{
		for(Long chunkId : nonexistentChunks)
		{
			if(chunkId.equals(id))
			{
				return null;
			}
		}
		
		Chunk chunk = chunks.get(id);
		
		if(chunk == null)
		{
			try
			{
				chunk = getChunkFromFiles(id);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			if(chunk == null)
			{
				nonexistentChunks.add(id);
			}
			else
			{
				chunks.put(id, chunk);
			}
		}
		
		return chunk;
	}

	private Chunk getChunkFromFiles(long id) throws IOException
	{
		System.out.println("Requesting chunk");
		toServer.write(1);
		toServer.writeLong(id);
		System.out.println("Sent request for chunk " + id);
		
		fromServer.readByte();
		
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
	
	public void paint(Graphics2D g2, Player player)
	{
		g2.drawImage(this.getChunk(player.cId).image, 0, 0, null);
		
		/*
		int viewRad = 2;
		int scale = 128;
		
		int offset = 0;
		
		Chunk northWest = getChunk(player.cId);
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
		
		Chunk meChunk = getChunk(player.cId);
		g2.drawImage(meChunk.image, 0 * scale, 0 * scale, scale, scale, null);
		*/
	}
}
