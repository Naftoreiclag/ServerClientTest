package me.naftoreiclag.survur;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient
{
	public static void main(String argv[]) throws Exception
	{
		String address = "localhost";
		int port = 1337;

		System.out.println("Establishing connection to " + address + ":" + 1337 + "...");
		Socket socket = new Socket(address, port);
		DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Connection Established!");

		System.out.print("Enter input: ");
		Scanner scanner = new Scanner(System.in);
		String userInput = scanner.nextLine();
		scanner.close();
		toServer.writeBytes(userInput + '\n');
		System.out.println("Sent: " + userInput);
		
		String serverInput = fromServer.readLine();
		System.out.println("Response from server: " + serverInput);
		socket.close();
	}
}
