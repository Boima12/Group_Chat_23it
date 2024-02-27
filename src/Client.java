import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.*;

public class Client {
	
	private Socket Client_Socket;
	private DataInputStream Client_Datain;
	private BufferedReader Client_Reader;
	private DataOutputStream Client_Dataout;
	private BufferedWriter Client_Writer;
	
	private String Client_SocketName;
	
	public Client(Socket sk1, String str1, JTextArea Ta1) {
		try {
			// setup Client_Socket
			Client_Socket = sk1;
			Client_SocketName = str1;
			
			// setup Client_Input
			Client_Datain = new DataInputStream(sk1.getInputStream());
			Client_Reader = new BufferedReader(new InputStreamReader(Client_Datain));
			
			// setup Client_Output
			Client_Dataout = new DataOutputStream(sk1.getOutputStream());
			Client_Writer = new BufferedWriter(new OutputStreamWriter(Client_Dataout));
			
			// sign up username for Handler
			Client_Username_Signup();
			
			System.out.println("Client connected succesfully!");
		} catch (IOException e) {
			closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
		}
	}
	
	public void Client_ReceiveMsg(JTextArea Ta1) {
		Thread thread_ReceiveMsg = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msgReceived;
					
					while (Client_Socket.isConnected()) {
						msgReceived = Client_Reader.readLine();
						
						Ta1.append(msgReceived);
						Ta1.append("\n");						
					}
				} catch (IOException e) {
					System.out.println("Client disconnected succesfully!");
					closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
				}
			}
		});
		
		thread_ReceiveMsg.start();
	}
	
	public void Client_Username_Signup() {
		try {
			Client_Writer.write(Client_SocketName);
			Client_Writer.newLine();
			Client_Writer.flush();
		} catch (IOException e) {
			System.out.println("Client_Username_Signup failed!");
			closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
		}
	}
	
	public void Client_SendMsg(JTextField Tf1) {
		try {
			Client_Writer.write(Tf1.getText());
			Client_Writer.newLine();
			Client_Writer.flush();
		
		} catch (IOException e) {
//			System.out.println("Client_SendMsg method broke!");
			closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
		}
	}
	
	public void closeEverything(Socket sk1, DataInputStream datain1, DataOutputStream dataout1, BufferedReader bfread1, BufferedWriter bfwrite1) {
		try {
			if (sk1 != null) {
				sk1.close();
			}
			if (datain1 != null) {
				datain1.close();
			}
			if (dataout1 != null) {
				dataout1.close();
			}
			if (bfread1 != null) {
				bfread1.close();
			}
			if (bfwrite1 != null) {
				bfwrite1.close();
			}
			
//			System.out.println("Server connection failed!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}