import java.io.*;
import java.net.*;
public class ProxyMain extends Thread  {
	public ProxyMain(){
		super("proxy main thread");
	}
	public void run(){
		ServerSocket serverSocket =null;
		int port = 10000;
		try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started on: " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port ");
            System.exit(-1);
        }
		//ServerSocket serverSocket = null;
		while(true){
			try {
			new ProxyThread(serverSocket.accept()).start();
			} catch (IOException e) {
				// ignire moi
				e.printStackTrace();
			}
		}
	}
}
