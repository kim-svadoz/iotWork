package can.basic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;
/* CAN통신을 위한 처리
 * 1. 수신시작하기 - 데이터를 수신할 준비가 되었음을 셋팅
 * 2. 데이터 수신하기
 * 3. 데이터 송신하기
 * 
*/
//캔통신하기 위한 정보 셋팅만 담당
public class CANConnect {
	SerialConnect serialConnect; //CAN시리얼 포트 연결
	OutputStream out; //CAN과 output통신할 스트림
	public CANConnect(String portname){
		//시리얼 통신을 위한 연결작업
		serialConnect = new SerialConnect();
		serialConnect.connect(portname,  this.getClass().getName());
		
		//input, output작업을 하기 위해 리스너를 port에 연결
		SerialPort commport = (SerialPort)serialConnect.getCommPort();
		SerialListener listener = new SerialListener(serialConnect.getBis());
		try {
			commport.addEventListener(listener);
			commport.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		//output스트림 받기
		out = serialConnect.getOut();
		
		//쓰레드가 실행될 수 있도록
		new Thread(new CanSerialWrite()).start();
		System.out.println("캔 통신이 시작되었습니다...");
	}
	//CAN통신을 하기 위한 쓰레드 - CAN output통신을 하는 쓰레드
	class CanSerialWrite implements Runnable{
		String data; // CAN으로 내보낼 데이터
		public CanSerialWrite() {
			//1. CAN통신을 할 수 있는 상태라는 것을 알려주기 - CAN수신을 설정
			this.data = ":G11A9\r"; // 수신시작에 관련된 데이터 프레임
		}
		
		public void run() {
			byte[] outputdata = data.getBytes();
			try {
				out.write(outputdata);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		new CANConnect("COM8");
	}
}
