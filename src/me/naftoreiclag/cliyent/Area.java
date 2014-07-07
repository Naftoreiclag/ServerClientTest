package me.naftoreiclag.cliyent;

import java.nio.ByteBuffer;

public class Area
{
	protected final long id;
	protected final Landmark[] landmarks;
	
	public Area(ByteBuffer data)
	{
		id = data.getLong();
		
		landmarks = new Landmark[data.get() & 0xFF];
		
		for(int i = 0; i < landmarks.length; ++ i)
		{
			landmarks[i] = new Landmark(data);
		}
	}
}
