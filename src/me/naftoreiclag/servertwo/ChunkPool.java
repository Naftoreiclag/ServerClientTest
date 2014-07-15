package me.naftoreiclag.servertwo;

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
	public static Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();
	public static List<Long> nonexistentChunks = new ArrayList<Long>();
	//public static Map<Long, Area> areas = new HashMap<Long, Area>();

	public static final String serverDirectory = "server/";
	
	public static Chunk getChunk(long id)
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
			chunk = getChunkFromFiles(id);
			
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

	private static Chunk getChunkFromFiles(long id)
	{
		byte[] chunkBytes = null;
		try
		{
			chunkBytes = Files.readAllBytes(Paths.get(serverDirectory + "map/chunks2/" + id + ".c"));
		}
		catch(NoSuchFileException e)
		{
			return null;
		}
		catch(IOException e)
		{
			return null;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(chunkBytes);
		
		return new Chunk(buffer);
	}
}
