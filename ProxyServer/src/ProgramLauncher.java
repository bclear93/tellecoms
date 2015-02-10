import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;


public class ProgramLauncher extends Thread{
	static ArrayList<String> blockedIp = new ArrayList<String>();
	static Lock newlock;
	//static int numberOfBlocked = 0;
	public ProgramLauncher(){
		super("ProxyThread");
	}
	public boolean isHostBlocked(String host){
		try{
			newlock.lock();
			if(blockedIp.contains(host)){
				return true;
			}else{
				return false;
			}
		}finally{
			newlock.unlock();
		}
		
		
	}
	public void run(){
		boolean exit = false;
		while(exit != true){
			System.out.println("(1)Do you want to block a host?");
			System.out.println("(2)Do you want to view blocked hosts?");
			System.out.println("(3)or do you want to exit?");
			Scanner input = new Scanner(System.in);
			int response = input.nextInt();
			switch(response){
			case 1:
				System.out.println("Please enter in the host you want to block\n");
				System.out.println("or type exit to go back");
				Scanner host = new Scanner(System.in);
				String hostToBlock = host.next();
				if(hostToBlock == "exit"){
					break;
				}else{
					if(blockedIp.contains(host)){
						System.out.println("this host is already blocked");
					}else{
						blockedIp.add(hostToBlock);
					}
				}
				break;
			case 2:
				for(int i = 0; i<blockedIp.size(); i++){
					System.out.println(blockedIp.get(i));
				}
				break;
			case 3:
				exit = true;
				break;
			}
		}
		System.out.println("exited");
	}
	public static void main(String[] args) {
		new ProgramLauncher().start();
		new ProxyMain().start();
		System.out.println("threads created");
	}
	
	public static void getBlockedIpList() {
		
	}

}
