package me.naftoreiclag.cliyent;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MapMaker
{
	public static void main(String[] args) throws IOException
	{
		doStuff();
	}
	
	public static void doStuff() throws IOException
	{
		List<ChunkEntry> entries = new ArrayList<ChunkEntry>();
		
		
		File[] fooFiles = new File("server/map/chunks2/").listFiles();
		for(File file : fooFiles)
		{
			byte[] data = Files.readAllBytes(file.toPath());
			
			ByteBuffer bb = ByteBuffer.wrap(data);
			
			long id = bb.getLong();
			long aid = bb.getLong();
			long wid = bb.getLong();
			long nid = bb.getLong();
			long eid = bb.getLong();
			long sid = bb.getLong();
			
			System.out.println("Chunk " + id + "|" + aid + " : " + wid + " " + nid + " " + eid + " " + sid);

			System.out.println("found" + id);
			entries.add(new ChunkEntry(id, wid, nid, eid, sid));
			
		}
		
		entries.get(0).setPos(0, 0);
		
		
		boolean mapFinished = false;
		while(!mapFinished)
		{
			mapFinished = true;
			for(int i = 1; i < entries.size(); ++ i)
			{
				ChunkEntry ii = entries.get(i);
				
				if(ii.posSet)
				{
					continue;
				}
				mapFinished = false;
				
				for(int j = 0; j < entries.size(); ++ j)
				{
					ChunkEntry jj = entries.get(j);
					
					if(!jj.posSet)
					{
						continue;
					}
					
					//System.out.println("id " + jj.id + " is at " + jj.x + ", " + jj.y);
					//System.out.println(jj.wid + " " + jj.nid + " " + jj.eid + " " + jj.sid);
					
					if(ii.id == jj.wid)
					{
						ii.setPos(jj.x - 1, jj.y);
						break;
					}
					else if(ii.id == jj.nid)
					{
						ii.setPos(jj.x, jj.y - 1);
						break;
					}
					else if(ii.id == jj.eid)
					{
						ii.setPos(jj.x + 1, jj.y);
						break;
					}
					else if(ii.id == jj.sid)
					{
						ii.setPos(jj.x, jj.y + 1);
						break;
					}
					else
					{
					}
						
				}
			}
			
		}
		
		System.out.println("success");
	}
	
	public static class ChunkEntry
	{
		int x = 0;
		int y = 0;
		
		boolean posSet = false;
		
		long id;
		long wid;
		long nid;
		long eid;
		long sid;
		
		public ChunkEntry(long id, long wid, long nid, long eid, long sid)
		{
			this.id = id;
			this.wid = wid;
			this.nid = nid;
			this.eid = eid;
			this.sid = sid;
			
		}
		
		public void setPos(int x, int y)
		{

			this.x = x;
			this.y = y;
			
		
			posSet = true;
		}
		
	}
}
