package me.naftoreiclag.test.copy;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.naftoreiclag.fileparsecommons.ParseCommons;

public class AreaToChunks
{
	public static long totally = 1337;
	
	public static void main(String[] args) throws IOException
	{
		convert("server/map/chunks2/", new File("tests/helloworld.area"));
	}
	
	public static Random random;
	
	static List<ChunkEntry> entries = new ArrayList<ChunkEntry>();
	
	public static void convert(String to, File from) throws IOException
	{
		byte[] fromBytes = Files.readAllBytes(from.toPath());
		ByteBuffer buff = ByteBuffer.wrap(fromBytes);
		
		
		AreaProject p = new AreaProject(buff);
		
		long areaId = randomLong();
		
		List<ChunkData> foo = new ArrayList<ChunkData>();
		
		for(int x = 0; x < p.cWidth; ++ x)
		{
			for(int y = 0; y < p.cHeight; ++ y)
			{
				foo.add(new ChunkData(p, areaId, x, y));
			}
		}
		
		
		for(ChunkData d : foo)
		{
			List<Byte> output = new ArrayList<Byte>();
			d.write(output);
			FooIOUtil.writeListToFile(new File(to + d.id + ".c"), output);
		}
		
	}
	
	public static class ChunkData
	{
		protected final long id;
		protected final long areaId;
		protected final boolean[][] collision = new boolean[16][16];

		protected final long wId;
		protected final long nId;
		protected final long eId;
		protected final long sId;
		
		protected final byte[][] landmarkData = new byte[16][16];
		protected final byte[][] pixelData = new byte[128][128];
		
		public ChunkData(AreaProject p, long areaId, int cx, int cy)
		{
			id = getChunkNum(cx, cy);
			
			this.areaId = areaId;
			
			wId = getChunkNum(cx - 1, cy);
			nId = getChunkNum(cx, cy - 1);
			eId = getChunkNum(cx + 1, cy);
			sId = getChunkNum(cx, cy + 1);
			
			for(int x = 0; x < 16; ++ x)
			{
				for(int y = 0; y < 16; ++ y)
				{
					collision[x][y] = p.collisionData[cx + x][cy + y];
					landmarkData[x][y] = (byte) p.landmarkData[cx + x][cy + y];
				}
			}
			
			for(int x = 0; x < 128; ++ x)
			{
				for(int y = 0; y < 128; ++ y)
				{
					pixelData[x][y] = p.pixelData[(cx << 7) + x][(cy << 7) + y];
				}
			}
		}
		
		public void write(List<Byte> bites)
		{
			addLong(bites, id);
			addLong(bites, 9001L);
			addLong(bites, wId);
			addLong(bites, nId);
			addLong(bites, eId);
			addLong(bites, sId);
			
			ParseCommons.writeCollisionArray(collision, 16, 16, bites);
			ParseCommons.writeUnalhaedByteArray(pixelData, 128, 128, bites);
		}
	}
	
	public static void addLong(List<Byte> bites, long l)
	{
		ByteBuffer b = ByteBuffer.allocate(Long.SIZE >> 3);
		
		b.putLong(l);
		
		b.flip();
		
		byte[] lol = b.array();
		
		for(byte bb : lol)
		{
			bites.add(bb);
		}
	}
	
	public static class ChunkEntry
	{
		int x;
		int y;
		long id;
		
		public ChunkEntry(int x, int y)
		{
			this.x = x;
			this.y = y;
			this.id = randomLong();
		}
	}

	public static long getChunkNum(int x, int y)
	{
		for(ChunkEntry e : entries)
		{
			if(e.x == x && e.y == y)
			{
				return e.id;
			}
		}
		
		ChunkEntry neu = new ChunkEntry(x, y);
		entries.add(neu);
		
		return neu.id;
	}
	
	public static long randomLong()
	{
		if(random == null)
		{
			random = new Random();
		}
		
		
		//return random.nextLong();
		
		return totally ++;
	}
}
