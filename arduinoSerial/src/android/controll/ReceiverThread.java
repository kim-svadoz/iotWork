package android.controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

//TCP서버쪽에 클라이언트의 요청을 계쏙 읽는 쓰레드
public class ReceiverThread extends Thread{
	Socket client;
	BufferedReader br; // 클라이언트의 메시지를 읽는 스트림
	PrintWriter pw; // 클라이언트에게 메시지를 전달하는 스트림
	SerialArduinoLEDControl serialObj; // 시리얼 통신을 위한 객체
	OutputStream os; // 시리얼통신에서 아두이노로 데이터를 내보내기 위한 스트림
	public ReceiverThread(Socket client) {
		this.client = client;
		//클라이언트가 보내오는 메시지를 읽기 위한 스트림 생성
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//클라이언트에게 메시지를 전송하기 위한 스트림 생성
			pw = new PrintWriter(client.getOutputStream(), true);
			//아두이노와 시리얼통신을 하기 위해 아두이노 쪽으로 데이터를 내보내기 위한 스트림 받기
			serialObj = new SerialArduinoLEDControl();
			serialObj.connect("COM8");
			os = serialObj.getOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		//클라이언트의 메시지를 받아서 아두이노로 데이터를 전송
		while(true) {
			try {
				String msg = br.readLine();
				System.out.println("클라이언트가 보내오는 메시지:"+msg);
				if(msg.equals("led_on")) {
					os.write('1');
				}else {
					os.write('0');
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
