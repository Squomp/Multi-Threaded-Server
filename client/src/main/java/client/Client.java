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
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("Address: " + s.getLocalSocketAddress());
			new ClientAcceptMessageThread(in).start();
			while (true) {
				System.out.print("Type a message: ");
				String message = br.readLine();
				out.println(message);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ClientAcceptMessageThread extends Thread {

		BufferedReader in;

		public ClientAcceptMessageThread(BufferedReader in) {
			this.in = in;
		}

		@Override
		public void run() {
			String answer;
			while (true) {
				try {
					answer = in.readLine();
					System.out.println(answer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
