package me.naftoreiclag.cliyent;

public class Area
{
	protected final long id;
	protected final Landmark[] landmarks;
	
	public Area(byte[] data, int byteIndex)
	{
		id = data[byteIndex ++];
		
		landmarks = new Landmark[data[byteIndex ++]];
		
		for(int i = 0; i < landmarks.length; ++ i)
		{
			landmarks[i] = new Landmark(data, byteIndex);
		}
	}
}
