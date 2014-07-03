package me.naftoreiclag.survur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server
{
	public static void main(String args[]) throws Exception
	{
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

			long requestedChunk = fromClient.readLong();
			System.out.println("Client wants chunk " + requestedChunk);

			byte[] chunkBytes = Files.readAllBytes(Paths.get("server/map/chunks/" + requestedChunk + ".chunk"));
			
			int fileSize = chunkBytes.length;
			
			toClient.writeInt(fileSize);
			System.out.println("Informed client that chunk " + requestedChunk + " is " + fileSize + "bytes.");
			
			toClient.write(chunkBytes, 0, chunkBytes.length);
			toClient.flush();
			System.out.println("Sent client chunk.");
			
			socket.close();
			System.out.println("Socket closed.");
		}
		
		serverSocket.close();
	}
}
