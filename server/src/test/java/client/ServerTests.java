package client;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.junit.Test;

public class ServerTests {
	
	public static boolean invalidPassed = false;

	@Test
	public void testValidSemaphoreAmount() {
		boolean passed = true;
		for (int i = 0; i < 7; i++) {
			try {
				new Thread(new Runnable() {

					public void run() {
						Socket s;
						try {
							s = new Socket("localhost", 3001);
							PrintWriter out = new PrintWriter(s.getOutputStream(), true);
							BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
							BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
							String answer;
							while (true) {
								try {
									answer = in.readLine();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				passed = false;
			}
		}
		assertEquals(true, passed);
	}

	@Test
	public void testInvalidSemaphoreAmount() {
		
		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {

				public void run() {
					Socket s;
					try {
						s = new Socket("localhost", 3001);
						SocketAddress sa = new InetSocketAddress("localhost", 3001);
						s.connect(sa, 1000);
						PrintWriter out = new PrintWriter(s.getOutputStream(), true);
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
						System.out.println("Address: " + s.getLocalSocketAddress());
						String answer;
						while (true) {
							try {
								answer = in.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (UnknownHostException e) {
//						e.printStackTrace();
					} catch (SocketException e) {
//						e.printStackTrace();
						invalidPassed = true;
					} catch (IOException e) {
//						e.printStackTrace();
					}
				}
			}).start();
		}
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		assertEquals(true, invalidPassed);
	}

}
