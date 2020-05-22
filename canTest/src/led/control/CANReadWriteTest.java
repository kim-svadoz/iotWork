package led.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.SerialPort;

public class CANReadWriteTest {
	SerialConnect serialConnect; // CAN�ø��� ��Ʈ ����
	OutputStream out; // CAN�� output����� ��Ʈ��

	public CANReadWriteTest(String portname) {
		// �ø��� ����� ���� �����۾�
		serialConnect = new SerialConnect();
		serialConnect.connect(portname, this.getClass().getName());

		// input, output�۾��� �ϱ� ���� �����ʸ� port�� ����
		SerialPort commport = (SerialPort) serialConnect.getCommPort();
		SerialListener listener = new SerialListener(serialConnect.getBis());
		try {
			commport.addEventListener(listener);
			commport.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		// output��Ʈ�� �ޱ�
		out = serialConnect.getOut();
		
		//ó�� CAN����� ���� �غ� �۾��� �� ���� ���Ű����� �޽����� ���� �� �ֵ���
		new Thread(new CANWriteThread()).start();
		
	}//end ������
	
	public void send(String msg) {
		new Thread(new CANWriteThread(msg)).start();
	}
	
	class CANWriteThread implements Runnable{
		//�۽��� �����͸� ������ ����
		String data;
		//************ó���� ���Ű��� ����*****************
		CANWriteThread(){
			this.data = ":G11A9\r";
		}
		//***********�޽��� ���������� ����*****************
		CANWriteThread(String msg){
			this.data = convert_data(msg);
		}
		public String convert_data(String msg) {
			msg = msg.toUpperCase(); // �޽����� �빮�ڷ� ��ȯ
			msg = "W28"+msg; // �۽ŵ������� ���б�ȣ�� �߰�
			//msg W28 00000000 000000000000000
			//�����������ӿ� ���� üũ���� ���� - �յڹ��� ���� �������� ���� �� 0xff�� &����.
			//String data = "W28000000000000000000000000";
			char[] data_arr = msg.toCharArray();
			int sum = 0;
			for(int i=0; i<data_arr.length; i++) {
				System.out.println(data_arr[i]);
				sum += data_arr[i];
			}
			sum = (sum & 0xff);
			String result = ":" +
						msg +
						Integer.toHexString(sum).toUpperCase() +
						"\r"; 
			return result;
		}
		@Override
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
		String id = "00000000"; // �۽��� �޽����� ����id
		String data = "0000000000000011"; // �۽��� ������
		String msg = id + data;
		CANReadWriteTest canObj = new CANReadWriteTest("COM5");
		canObj.send(msg);
	}

}
