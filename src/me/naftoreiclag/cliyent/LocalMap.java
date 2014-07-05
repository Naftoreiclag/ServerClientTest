package me.naftoreiclag.cliyent;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class LocalMap
{
	public Map<Long, Area> areas = new HashMap<Long, Area>();
	public Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();
	
	public void paint(Graphics2D g2, Player player)
	{
		int viewRad = 2;
		int scale = 64;
		
		Chunk northWest = chunks.get(player.chunk);
		for(int _ = 0; _ < viewRad; ++ _)
		{
			northWest = chunks.get(northWest.nID);
			northWest = chunks.get(northWest.wID);
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
				if(x != viewRad * 2) { pinpointB = chunks.get(pinpointB.eID); }
			}

			// Move pointer A south, unless this is the last in the rows, in which case don't do anything because that's pointless.
			if(y != viewRad * 2) { pinpointA = chunks.get(pinpointA.sID); }
		}
	}
}
