package driving;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread{
	Socket client;
	BufferedReader br;//클라이언트의 메시지를 읽는 스트림
	PrintWriter pw; // 클라이언트에게 메시지를 전달하는 스트림
	SerialArduinoDrivingControl serialObj;
	OutputStream os;
	
	public ReceiverThread(Socket client) {
		this.client = client;
		try {
			// 클라이언트가 보내오는 메시지를 읽기 위한 스트림 생성
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			// 클라이언트에게 메시지를 전송하기 위한 스트림 생성
			pw = new PrintWriter(client.getOutputStream(),true);
			// 아두이노와 시리얼통신을 위해서 아두이노로 데이터를 내보내기 위한 스트림얻기
			serialObj = new SerialArduinoDrivingControl();
			serialObj.connect("COM5");
			os = serialObj.getOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//클라이언트의 메시지르 ㄹ받아서 아두이노로 데이터를 전송
		while(true) {
			String msg;
			try {
				msg= br.readLine();
				if(msg!=null) {
					System.out.println("클라이언트가 보내오는 메시지: "+msg);
					if(msg.equals("forward")) {
						os.write('f');
					} else if (msg.equals("left")) {
						os.write('x');
					} else if (msg.equals("right")) {
						os.write('y');
					} else if(msg.equals("back")) {
						os.write('b');
					} else if(msg.equals("up")) {
						os.write('u');
					} else if(msg.equals("down")) {
						os.write('d');
					} else if (msg.equals("leftspin")) {
						os.write('l');
					} else if (msg.equals("rightspin")) {
						os.write('r');
					} else if (msg.equals("stop")) {
						os.write('s');
					} 
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}
