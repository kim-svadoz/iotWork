package driving;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AndroidDrivingControlServer {
	private ServerSocket server;
	
	
	public void connect() {
		try {
			server = new ServerSocket(33334);
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						System.out.println("���Ӵ��");
						Socket client = server.accept();
						System.out.println("����");
						String ip = client.getInetAddress().getHostAddress();
						System.out.println(ip+"���������!!\n");
						new ReceiverThread(client).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
	public static void main(String[] args) {
		new AndroidDrivingControlServer().connect();
	}

}
