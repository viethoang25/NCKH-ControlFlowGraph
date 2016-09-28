package ver1;

import java.io.DataInputStream;
import java.net.Socket;

public class InputThread implements Runnable {

	Socket socket;
	String inputMessage;

	public InputThread(Socket socket) {
		this.socket = socket;
	}

	
	public String getInputMessage() {
		return inputMessage;
	}

	@Override
	public void run() {
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(
						socket.getInputStream());
				inputMessage = dis.readUTF();
				System.out.println(inputMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

