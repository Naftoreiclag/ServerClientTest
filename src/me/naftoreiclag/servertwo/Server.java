package me.naftoreiclag.servertwo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server
{
	public static final String serverDirectory = "server/";
	
	public static long[] spawnChunks;
	
	public static void loadSpawnChunks() throws IOException
	{
		List<Long> fooList = new ArrayList<Long>();
		
		File[] fooFiles = new File("server/map/chunks2/").listFiles();
		for(File file : fooFiles)
		{
			try
			{
				
			}
			catch(Exception e)
			{
				continue;
			}
			
			fooList.add(Long.parseLong(file.getName().substring(0, file.getName().length() - 2)));
		}
		

		spawnChunks = new long[fooList.size()];
		for(int  i= 0; i <            fooList.size(); ++ i)
		{
			spawnChunks[i] = fooList.get(i);
			
			
		}

		System.out.println("Spawn chunk size: " + spawnChunks.length);
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

		int debugId = 0;
		
		boolean running = true;
		while(running)
		{
			Socket socket = serverSocket.accept();
			ServicingThread st = new ServicingThread(socket, debugId ++);
			st.run();
		}
		
		serverSocket.close();
	}
	
	public static class ServicingThread extends Thread
	{
		public final Player player;
		
		public final Socket socket;
		
		public final DataOutputStream toClient;
		public final DataInputStream fromClient;
		
		public final int debugId;
		
		public ServicingThread(Socket socket, int debugId) throws IOException
		{
			this.socket = socket;
			
			this.toClient = new DataOutputStream(socket.getOutputStream());
			this.fromClient = new DataInputStream(socket.getInputStream());
			
			say("Connection Established!");
			
			player = new Player(spawnChunks[15], (byte) 8, (byte) 8);
			
			this.debugId = debugId;
		}
		
		private void say(String s)
		{
			System.out.println(debugId + " " + s);
		}
		
		private void sayErr(String s)
		{
			System.err.println(debugId + " " + s);
		}
		
		@Override
		public void run()
		{	
			try
			{
				sendPlayerLocationUpdate();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(true)
			{
				say("Waiting for client to request something...");
				try
				{
					byte requestType;
					requestType = fromClient.readByte();

					if(requestType == 0x00)
					{
						say("Client wants to disconnect.");

						break;
					}
					else if(requestType == 0x01)
					{
						say("Client wants a chunk.");
						say("Waiting for client to specify chunk...");
						long requestedChunk = fromClient.readLong();
						say("Client wants chunk " + requestedChunk);

						sendChunk(requestedChunk);
					}
					else if(requestType == 0x02)
					{
						say("Client wants to move west.");
						if(player.moveWest())
						{
							sendPlayerLocationUpdate();
						}
					}
					else if(requestType == 0x03)
					{
						say("Client wants to move north.");
						if(player.moveNorth())
						{
							sendPlayerLocationUpdate();
						}
					}
					else if(requestType == 0x04)
					{
						say("Client wants to move east.");
						if(player.moveEast())
						{
							sendPlayerLocationUpdate();
						}
					}
					else if(requestType == 0x05)
					{
						say("Client wants to move south.");
						if(player.moveSouth())
						{
							sendPlayerLocationUpdate();
						}
					}
				}
				catch(IOException e)
				{
					sayErr("Communiation with client lost: " + e.getLocalizedMessage());

					break;
				}
			}
			
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				sayErr("Issue closing socket: " + e.getLocalizedMessage());
			}
			say("Socket closed.");
		}
		
		private void sendChunk(long requestedChunk) throws IOException
		{
			toClient.write(0x01);
			
			byte[] chunkBytes = null;
			try
			{
				chunkBytes = Files.readAllBytes(Paths.get(serverDirectory + "map/chunks2/" + requestedChunk + ".c"));
			}
			catch(NoSuchFileException e)
			{
				sayErr("Chunk " + requestedChunk + " does not exist.");
				toClient.writeInt(0);
				sayErr("Client informed of this.");
			}
			catch(IOException e)
			{
				sayErr("Error reading chunk" + requestedChunk + ": " + e.getLocalizedMessage());
				toClient.writeInt(0);
				sayErr("Client informed of this.");
			}

			if(chunkBytes != null)
			{
				int fileSize = chunkBytes.length;

				toClient.writeInt(fileSize);
				say("Informed client that chunk " + requestedChunk + " is " + fileSize + "bytes.");

				toClient.write(chunkBytes, 0, chunkBytes.length);
				toClient.flush();
				say("Sent client chunk.");
			}
		}

		private void sendPlayerLocationUpdate() throws IOException
		{
			toClient.write(0x02);
			
			toClient.writeLong(player.cId);
			toClient.writeByte(player.x);
			toClient.writeByte(player.y);
		}
	}
}
