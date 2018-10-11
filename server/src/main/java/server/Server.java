package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	public static final int THREAD_COUNT = 10;
	private ServerSocket ss;
	private Socket client;
	private int port = 3001;
	private ExecutorService es;
	
	public Server() {
		es = Executors.newFixedThreadPool(THREAD_COUNT);
		while (true) {
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(port);
				client = serverSocket.accept();
				// start thread
				es.execute(new ServerThread(client));
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
	
	class ServerThread extends Thread {
		
		private Socket client;
		private PrintWriter out;
		private BufferedReader in;
		
		
		public ServerThread(Socket client) {
			this.client = client;
			try {
				this.out = new PrintWriter(client.getOutputStream(), true);
				this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				String message = in.readLine();
				out.println(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
