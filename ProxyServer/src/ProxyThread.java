import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

public class ProxyThread extends Thread {
	private Socket socket = null;

	public ProxyThread(Socket socket) {
		// super("ProxyThread");
		this.socket = socket;
	}

	public void getHost(InputStream inFromClient) {

		byte[] barr = null;
		barr = new byte[65536];
		try {
			inFromClient.read(barr, 0, barr.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(new String(barr));
		String s = new String(barr);
		String host = "";
		String[] list = s.split("\n");
		for (int i = 0; i < list.length; i++) {
			if (list[i].contains("Host:")) {
				host = list[i].substring(6).trim();
			}
		}
	}
//	public boolean isBlocked(){
//		
//	}

	public void run() {
		try {
			ProgramLauncher newLauncher = new ProgramLauncher();
			byte[] newByte = new byte[2000];
			File f = new File("blockedWebpage.html");
			FileInputStream  fileread = new FileInputStream(f);
			fileread.read(newByte);
			
			System.out.println("Started thread");
			OutputStream outToClient = socket.getOutputStream();
			InputStream inFromClient = socket.getInputStream();

			byte[] barr = null;
			barr = new byte[65536];
			inFromClient.read(barr, 0, barr.length);
			System.out.println("Got initial data from user");
			String s = new String(barr);
			String host = "";
			String[] list = s.split("\n"); // parse the string s into an array
											// of strings seperated by new line
			for (int i = 0; i < list.length; i++) {
				if (list[i].contains("Host:")) { // whenever the a string in the
													// array has "Host:"
					host = list[i].substring(6).trim();
				}
			}
			System.out.println("Got the host: " + host);
			if(newLauncher.isHostBlocked(host)){
				System.out.println("this host is blocked");
				outToClient.write(newByte);
				
			}else{
				Socket newSocket = new Socket(host, 80);
				System.out.println("Created socket to host");
				OutputStream outToHost = newSocket.getOutputStream();
				InputStream inFromHost = newSocket.getInputStream();
	
				System.out.println("Wrote initial data to host");
				outToHost.write(barr);
				outToHost.flush();
	
				System.out.println("Read in initial response from host");
				barr = new byte[65536];
				inFromHost.read(barr, 0, barr.length);
	
				System.out.println("Returned initial response to user");
				outToClient.write(barr);
				outToClient.flush();
	
				int size;
				while (true) {
					try {
						if (inFromClient.available() != 0) {
							System.out.println("Client has data to send");
							barr = new byte[65536];
							size = inFromClient.read(barr, 0, barr.length);
							byte[] toHost = new byte[size];
							System.arraycopy(barr, 0, toHost, 0, size);
							System.out.println("Wrote data to host");
							outToHost.write(toHost);
							outToHost.flush();
						}
						if (inFromHost.available() != 0) {
							System.out.println("Host has data to return");
							barr = new byte[65536];
							size = inFromHost.read(barr, 0, barr.length);
							byte[] toHost = new byte[size];
							System.arraycopy(barr, 0, toHost, 0, size);
							System.out.println("Returned data to user");
							outToClient.write(toHost);
							outToClient.flush();
						}
						if (inFromClient.available() == 0
								&& inFromHost.available() == 0) {
							System.out
									.println("No data from host/user going to sleep");
							try {
								System.out.println("Going to sleep");
								sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (inFromClient.available() == 0
									&& inFromHost.available() == 0) {
								System.out.println("Still no data, finishing");
								break;
							}
						}
					} catch (IOException e) {
						System.out.println("Exception: " + e.getMessage());
					}
				}
			}
			System.out.println("Done");
			// int newS = 0;

			// barr = new byte[65536];
			// try{
			// System.out.println("here2");
			// if(in.available()!=0){
			// System.out.println("here3");
			// while((in.read(barr)) != -1){
			// //in.read(barr, 0, barr.length);
			// System.out.println("here7");
			// newOut.write(barr);
			//
			// }
			// newOut.flush();
			// }
			// } catch (IOException e) {
			// System.out.println("this222");
			// }
			//
			// barr = new byte[65536];
			// int sfu;
			// try{
			// if(newIn.available()!=0){
			// System.out.println("11111here");
			// while((sfu = newIn.read(barr)) != -1){
			// System.out.println("here5");
			// sfu += sfu;
			// System.out.println(sfu);
			// out.write(barr);
			// out.flush();
			// }
			//
			// }
			// } catch (IOException e) {
			// System.out.println("this");
			// }
			//
			// System.out.println("end");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
