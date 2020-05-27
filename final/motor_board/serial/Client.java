package driving2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import can.CANReadWriteTest;

public class Client extends Thread {
	String nickname;
	Socket socket;

	BufferedReader br;
	PrintWriter pw;
	SerialArduinoDrivingControl serialObj;
	OutputStream os;
	static CANReadWriteTest canObj;
	public void connectServer() {
		try {
			socket = new Socket(Variable.ip, Variable.port);
			if (socket != null) {
				ioWork();
				new ReceiverThread(os, br).start();
			}
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	public void ioWork() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			serialObj = new SerialArduinoDrivingControl(pw);
			serialObj.connect(Variable.Serialport);
			os = serialObj.getOutput();
			canObj = new CANReadWriteTest("COM10",pw);
		} catch (IOException e) {
		}
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.connectServer();
	}
}
