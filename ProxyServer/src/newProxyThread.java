import java.net.*;
import java.io.*;
import java.util.*;
public class newProxyThread extends Thread {
	private Socket socket = null;
	public newProxyThread(Socket socket) {
//        super("ProxyThread");
        this.socket = socket;
    }
	
	public void run() {
		System.out.println("Ran");
		try {
//			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//output stream to client
			OutputStream outToClient = socket.getOutputStream();
			//input stream from client
			InputStream inFromClient = socket.getInputStream();
			
			//this code is used to get the host name
			
			byte[] buffer = null;
			buffer = new byte[65536];	//create a byte array of large size
			int stuff;
			stuff = inFromClient.read(buffer, 0, buffer.length);
			String s = new String(buffer);	//convert the byte array to a string
			//System.out.println(s);
			String host =  "";				//declare the host name string
			String[] list = s.split("\n");	//parse the string s into an array of strings seperated by new line
			for(int i=0; i<list.length; i++){	
				if(list[i].contains("Host:")){	//whenever the a string in the array has "Host:" 
					host = list[i].substring(6).trim();	//take the substring of that string(minus the "Host:")
														//and let that equal host, this is the host name!
				}
			}
			
			System.out.println(host);
			//create new socket using the host name. This socket connects to the host ie google, reddit etc.
			Socket newSocket = new Socket(host, 80);
			
			//new output stream to the host
			OutputStream outToHost = newSocket.getOutputStream();
			//new input stream from the host
			InputStream inFromHost = newSocket.getInputStream();
			
			
			//barr = new byte[65536];
			try{
				System.out.println("here2");
				if(inFromClient.available()==0){
					System.out.println("no more in prom client");
					outToHost.write(buffer);
				}
				else{
					while((inFromClient.read(buffer)) != -1){
						//in.read(barr, 0, barr.length);
						System.out.println("here3");
						outToHost.write(buffer);
						outToHost.flush();
					}
					
				}
						
				//}
			} catch (IOException e) {
				System.out.println("this1");
            }
			
//			try { 
//				outToHost.close(); 
//			} catch (IOException e) {
//					
//			}
			
			
			int newS = 0;
			byte[] barr;
			barr = new byte[65536];
			try{
				//if(newIn.available()!=0){
					System.out.println("here");
					while((newS = inFromHost.read(barr)) != -1){
						System.out.println("here5");
						//newS = in.read(barr, 0, barr.length);
						System.out.println(newS + ": " + barr);	
						outToClient.write(barr);
						outToClient.flush();
					}
				//}
			} catch (IOException e) {
				e.printStackTrace();
            }
			
			try { 
				outToClient.close(); 
				outToHost.close();
			} catch (IOException e) {
				
			}
			System.out.println("end");
			
			
			
			//read into barr the response from the host
			
			
            
			
			//This is for larger streams. for if the client or the host have more data to send
//			while(bool){
//				//check if the client has any data to send, if it does:
//				if(in.available()!=0){
//					//reallocate the barr array, and read in the data from the client into it
//					barr = new byte[65536];
//					in.read(barr, 0, barr.length);
//					
//					//write that data to the host
//					newOut.write(barr);
//					newOut.flush();					
//				}
//				//check if the host has something to send
//				if(newIn.available()!= 0){
//					//read the data in from the host
//					barr = new byte[65536];
//					newIn.read(barr, 0, barr.length);
//					
////					String ss = new String(barr);
////					System.out.println(barr);
//					
//					i++;
//					
//					//write that data read from the host into the client
//					out.write(barr);
//					out.flush();
//				}
//				if(in.available()==0 && newIn.available() == 0){
//					bool = false;
//				}
//				
//				System.out.println(i);
//			}
//			int stuff;
//			barr = new byte[65536];
//			try{
//				//if(in.available()!=0){
//					while((stuff = in.read(barr)) != -1){
//						//in.read(barr, 0, barr.length);
//						
//						newOut.write(barr);
//						
//					}
//					newOut.flush();	
//				//}
//			} catch (IOException e) {
//            }
			
//			barr = new byte[65536];
//			try{
//				int newS;
//				//if(newIn.available()!=0){
//					while((newS = newIn.read(barr)) != -1){
//						//in.read(barr, 0, barr.length);
//						
//						out.write(barr);
//							
//					}
//					out.flush();
//				//}
//			} catch (IOException e) {
//            }
//			
		} catch (IOException e) {
			// iglnorei moiioi
			e.printStackTrace();
		}
	}
}