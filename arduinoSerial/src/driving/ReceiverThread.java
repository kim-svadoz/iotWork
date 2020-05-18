package driving;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread{
	Socket client;
	BufferedReader br;//Ŭ���̾�Ʈ�� �޽����� �д� ��Ʈ��
	PrintWriter pw; // Ŭ���̾�Ʈ���� �޽����� �����ϴ� ��Ʈ��
	SerialArduinoDrivingControl serialObj;
	OutputStream os;
	
	public ReceiverThread(Socket client) {
		this.client = client;
		try {
			// Ŭ���̾�Ʈ�� �������� �޽����� �б� ���� ��Ʈ�� ����
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			// Ŭ���̾�Ʈ���� �޽����� �����ϱ� ���� ��Ʈ�� ����
			pw = new PrintWriter(client.getOutputStream(),true);
			// �Ƶ��̳�� �ø�������� ���ؼ� �Ƶ��̳�� �����͸� �������� ���� ��Ʈ�����
			serialObj = new SerialArduinoDrivingControl();
			serialObj.connect("COM5");
			os = serialObj.getOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//Ŭ���̾�Ʈ�� �޽����� ���޾Ƽ� �Ƶ��̳�� �����͸� ����
		while(true) {
			String msg;
			try {
				msg= br.readLine();
				if(msg!=null) {
					System.out.println("Ŭ���̾�Ʈ�� �������� �޽���: "+msg);
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
