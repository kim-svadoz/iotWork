package led.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//CAN�ϰ� ����� �� �ִ� Listener
//�ø��� ����� ���ؼ� �����Ͱ� ���۵Ǿ��� �� ����Ǵ� Ŭ����
public class SerialListener implements SerialPortEventListener{
	BufferedInputStream bis; // ĵ������� input�Ǵ� �����͸� �б� ���� ���µ� ��Ʈ��
	SerialConnect arduinoConnect; // �Ƶ��̳�� �ø�������� ���� ��ü
	OutputStream os; // �Ƶ��̳�� �ø�������� ���� �۾�
	public SerialListener(BufferedInputStream bis) {
		this.bis = bis;
		arduinoConnect = new SerialConnect();
		arduinoConnect.connect("COM8", "arduino");
		os = arduinoConnect.getOut();
	}
	
	//�����Ͱ� ���۵ɶ����� ȣ��Ǵ� �޼ҵ�
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()) {
		//�����Ͱ� �����ϸ�
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[128];
			try {
				while(bis.available()>0) {
					int numbyte = bis.read(readBuffer);
				}
				String data = new String(readBuffer);
				System.out.println("CAN�ø��� ��Ʈ�� ���۵� ������ ===> "+data);
				//���۵Ǵ� �޽����� �˻��ؼ� �����ϰ� �ٸ� ��ġ�� �����ϰų�
				//CarŬ���̾�Ʈ ��ü�� �����ؼ� ������ ���۵ǵ��� ó��
				//CAN���� ���ŵ� �����Ͱ� 0000000000000011�̸� LED ����
				//CAN���� ���ŵ� �����Ͱ� 0000000000000000�̸� LED �ѱ�
				/*
				 * 1. �Ƶ��̳�� �ø�������� �� �ֵ��� output��Ʈ�����
				 * 	=> �����ڿ��� �� ���� �۾��� �� �ֵ��� ó��
				 * 2. output��Ʈ������ '0','1' ��������
				 * 	=> CAN���� ���ŵ� �����͸� ���ؼ�
				 * : U280000000000000000000000003F
				*/
				if(os!=null) {
					if(data.trim().equals(":U280000000000000000000000003F")) {
						os.write('1');
					}else {
						os.write('0');
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
