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
		
		Chunk northWest = chunks.get(player.locationChunkID);
		for(int _ = 0; _ < viewRad; ++ _)
		{
			northWest = chunks.get(northWest.nId);
			northWest = chunks.get(northWest.wId);
		}
		
		// Pointer that stays in the west-most column and moves south with y
		Chunk pinpointA = chunks.get(0);
		for(int y = 0; y <= viewRad * 2; ++ y)
		{
			// Pointer that is the same row as A but moves east with x
			Chunk pinpointB = pinpointA;
			for(int x = 0; x <= viewRad * 2; ++ x)
			{
				g2.drawImage(pinpointB.image, x * scale, y * scale, scale, scale, null);
				
				// Move pointer B east, unless this is the last in the columns, in which case don't do anything because that's pointless.
				if(x != viewRad * 2) { pinpointB = chunks.get(pinpointB.eId); }
			}

			// Move pointer A south, unless this is the last in the rows, in which case don't do anything because that's pointless.
			if(y != viewRad * 2) { pinpointA = chunks.get(pinpointA.sId); }
		}
	}
	
	public Chunk getChunk(long iD)
	{
		Chunk trY = chunks.get(iD);
		
		if(trY == null)
		{
			try
			{
				trY = getChunkFromServer(iD);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return trY;
	}
	
	public Chunk getChunkFromServer(long id) throws Exception
	{
		System.out.println("Requesting chunk");
		toServer.writeLong(id);
		System.out.println("Sent request for chunk " + id);
		
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
	    
	    
		
		return new Chunk(chunkBytes, 0);
	}
}
