package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public Client() {
		Socket s;
		try {
			s = new Socket("localhost", 3001);
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			String message = "yeet";
			out.println(message);
			System.out.println("Sent: " + message);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String answer = in.readLine();
			System.out.println("Server said: " + answer);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
