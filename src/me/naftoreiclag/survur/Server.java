package me.naftoreiclag.survur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Random;

public class Server
{
	public static final String serverDirectory = "server/";
	
	public static long[] spawnChunks;
	
	public static void loadSpawnChunks() throws IOException
	{
		byte[] chunkBytes = Files.readAllBytes(Paths.get(serverDirectory + "map/spawnchunks.s"));
		
		spawnChunks = new long[chunkBytes.length >> 3];
		
		for(int i = 0; i < spawnChunks.length; ++ i)
		{
			spawnChunks[i] = chunkBytes[i << 3];
			for(int j = 1; j < 8; ++ j)
			{
				spawnChunks[i] += chunkBytes[(i << 3) + j];
			}
		}
		
		System.out.print("Spawn chunk list: ");
		for(long id : spawnChunks)
		{
			System.out.print(id);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public static void main(String args[]) throws Exception
	{
		loadSpawnChunks();
		
		ServerSocket serverSocket = new ServerSocket(1337);

		boolean running = true;
		while(running)
		{
			System.out.println("Waiting for client...");
			Socket socket = serverSocket.accept();
			System.out.println("Client found!");
			
			DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
			DataInputStream fromClient = new DataInputStream(socket.getInputStream());
			System.out.println("Connection Established!");
			
			toClient.writeLong(spawnChunks[(new Random()).nextInt(spawnChunks.length)]);
			System.out.println("Sent a random spawn chunk's ID");
			
			boolean conversationSustained = true;
			while(conversationSustained)
			{
				System.out.println("Waiting for client to request something...");
				byte requestType;
				try
				{
					requestType = fromClient.readByte();
				}
				catch(Exception e)
				{
					System.err.println("Communiation with client lost: " + e.getLocalizedMessage());
					
					conversationSustained = false;
					break;
				}
				
				if(requestType == 0)
				{
					System.out.println("Client wants to disconnect.");
				}
				else if(requestType == 1)
				{
					System.out.println("Client wants a chunk.");
					System.out.println("Waiting for client to specify chunk...");
					long requestedChunk = fromClient.readLong();
					System.out.println("Client wants chunk " + requestedChunk);
					
					
					byte[] chunkBytes = null;
					try
					{
						chunkBytes = Files.readAllBytes(Paths.get(serverDirectory + "map/chunks/" + requestedChunk + ".c"));
					}
					catch(NoSuchFileException e)
					{
						System.out.println("Chunk " + requestedChunk + " does not exist.");
						toClient.writeInt(0);
						System.out.println("Client informed of this.");
					}
					
					if(chunkBytes != null)
					{
						int fileSize = chunkBytes.length;
						
						toClient.writeInt(fileSize);
						System.out.println("Informed client that chunk " + requestedChunk + " is " + fileSize + "bytes.");
						
						toClient.write(chunkBytes, 0, chunkBytes.length);
						toClient.flush();
						System.out.println("Sent client chunk.");
					}
				}
			}
			
			socket.close();
			System.out.println("Socket closed.");
		}
		
		serverSocket.close();
	}
}
