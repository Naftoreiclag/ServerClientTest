package me.naftoreiclag.servertwo;

public class Player
{
	protected byte x;
	protected byte y;
	
	protected long cId;
	
	protected Player(long cId, byte x, byte y)
	{
		this.cId = cId;
		
		this.x = x;
		this.y = y;
	}
	
	protected boolean moveWest()
	{
		if(x == 0)
		{
			Chunk c = ChunkPool.getChunk(ChunkPool.getChunk(cId).wId);
			
			if(c == null)
			{
				return false;
			}
			else
			{
				cId = c.id;
				x = 15;
				
				return true;
			}
		}
		else
		{
			-- x;
			return true;
		}
	}
	
	protected boolean moveNorth()
	{
		if(y == 0)
		{
			Chunk c = ChunkPool.getChunk(ChunkPool.getChunk(cId).nId);
			
			if(c == null)
			{
				return false;
			}
			else
			{
				cId = c.id;
				y = 15;
				
				return true;
			}
		}
		else
		{
			-- y;
			return true;
		}
	}
	
	protected boolean moveEast()
	{
		if(x == 15)
		{
			Chunk c = ChunkPool.getChunk(ChunkPool.getChunk(cId).eId);
			
			if(c == null)
			{
				return false;
			}
			else
			{
				cId = c.id;
				x = 0;
				
				return true;
			}
		}
		else
		{
			++ x;
			return true;
		}
	}
	
	protected boolean moveSouth()
	{
		if(y == 15)
		{
			Chunk c = ChunkPool.getChunk(ChunkPool.getChunk(cId).sId);
			
			if(c == null)
			{
				return false;
			}
			else
			{
				cId = c.id;
				y = 0;
				
				return true;
			}
		}
		else
		{
			++ y;
			return true;
		}
	}
	
	
}
