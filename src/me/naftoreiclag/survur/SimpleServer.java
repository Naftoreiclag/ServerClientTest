package me.naftoreiclag.survur;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer
{
	public static void main(String args[]) throws Exception
	{
		ServerSocket serverSocket = new ServerSocket(1337);

		boolean running = true;
		while(running)
		{
			System.out.println("Waiting for client...");
			Socket connectionSocket = serverSocket.accept();
			System.out.println("Client found!");
			
			System.out.println("Establishing connection...");
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Connection Established!");

			System.out.println("Waiting for input...");
			String clientInput = fromClient.readLine();
			System.out.println("Input received! Input: " + clientInput);
			
			String responseClient = clientInput.toUpperCase();
			toClient.writeBytes(responseClient + '\n');
			System.out.println("Response: " + responseClient);
		}
		
		serverSocket.close();
	}
}
