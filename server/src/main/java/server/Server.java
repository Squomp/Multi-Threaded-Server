package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {

	public static final int THREAD_COUNT = 10;
	private ServerSocket ss;
	private Socket client;
	private int port = 3001;
	private ExecutorService es;
	private static List<Socket> clients = new ArrayList<Socket>();
	private static Object yeet = new Object();
	private Semaphore semaphore = new Semaphore(THREAD_COUNT);

	public Server() {
		es = Executors.newCachedThreadPool();
		try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				if (semaphore.tryAcquire()) {
					synchronized (yeet) {
						client = ss.accept();
						System.out.println("Connection accepted " + clients.size());
						// start thread
						Thread t = new ServerThread(client);
						clients.add(client);
						es.execute(t);
					}
				} else {
					client = ss.accept();
					client.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
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
				this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				String message = in.readLine();
				sendMessage(client.getRemoteSocketAddress() + " " + message);
			} catch (Exception e) {
				// e.printStackTrace();
				// semaphore.release();
			}

		}

		private void sendMessage(String message) {
			synchronized (yeet) {
				for (Socket s : clients) {
					if (!s.equals(client)) {
						try {
							out = new PrintWriter(s.getOutputStream(), true);
							out.println(message);
							out.flush();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

}
