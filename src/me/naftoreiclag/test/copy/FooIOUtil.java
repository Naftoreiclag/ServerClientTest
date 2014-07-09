package me.naftoreiclag.test.copy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;

public class FooIOUtil
{
	public static ByteBuffer loadBufferFromFile(File file)
	{
		byte[] data = null;
		try
		{
			data = Files.readAllBytes(file.toPath());
		}
		catch (IOException e2) { e2.printStackTrace(); }
		return ByteBuffer.wrap(data);
	}
	
	public static BufferedImage loadImageFromFile(File file)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(file);
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		
		return image;
	}
	
	public static void writeListToFile(File file, List<Byte> bites) throws IOException
	{
		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
		
		System.out.println("saved " + data.length);
	}
}
