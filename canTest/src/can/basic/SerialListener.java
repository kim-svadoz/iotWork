package can.basic;

import java.awt.Event;
import java.io.BufferedInputStream;
import java.io.IOException;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//CAN하고 통신할 수 있는 Listener
//시리얼 통신을 통해서 데이터가 전송되었을 때 실행되는 클래스
public class SerialListener implements SerialPortEventListener{
	BufferedInputStream bis; // 캔통신으로 input되는 데이터를 읽기 위해 오픈된 스트림
	public SerialListener(BufferedInputStream bis) {
		this.bis = bis;
	}
	
	//데이터가 전송될때마다 호출되는 메소드
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()) {
		//데이터가 도착하면
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[128];
			try {
				while(bis.available()>0) {
					int numbyte = bis.read(readBuffer);
				}
				String data = new String(readBuffer);
				System.out.println("CAN시리얼 포트로 전송된 데이터 ===> "+data);
				//전송되는 메시지를 검사해서 적절하게 다른 장치를 제어하거나
				//Car클라이언트 객체로 전달해서 서버로 전송되도록 처리
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
