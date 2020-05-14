package basic;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

public class SerialConnectionTest {

	public SerialConnectionTest() {
		
	}
	public void connect(String portName) {
		try {
			//COM��Ʈ�� ���� �����ϰ� ��밡���� �������� Ȯ��
			CommPortIdentifier commPortIdentifier = 
					CommPortIdentifier.getPortIdentifier(portName);
			if(commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("��Ʈ ����� �� �����ϴ�.");
			}else {
				System.out.println("��Ʈ�� ��� ����.");
				//port�� ��� �����ϸ� ��Ʈ�� ���� ��Ʈ��ü�� ������
				//�Ű�����1 : ��Ʈ�� ���� ����ϴ� ���α׷��� �̸�
				//�Ű�����2 : ��Ʈ�� ���� ����ϱ� ���ؼ� ��ٸ��� �ð�
				CommPort commPort = commPortIdentifier.open("basic_serial", 3000);
				System.out.println(commPort);
			}
		} catch (NoSuchPortException | PortInUseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new SerialConnectionTest().connect("COM8"); 
	}

}
