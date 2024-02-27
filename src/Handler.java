import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Handler {
	
	private Socket Handler_Socket;
	private DataInputStream Handler_Datain;
	private BufferedReader Handler_Reader;
	private DataOutputStream Handler_Dataout;
	private BufferedWriter Handler_Writer;
	
	private String Handler_SocketName;
	public static ArrayList<Handler> Handler_Sockets = new ArrayList<>();
	
	public Handler(Socket sk1) {
		try {
			// setup Handler_Socket
			Handler_Socket = sk1;
			Handler_Sockets.add(this);
			
			// setup Handler_Input
			Handler_Datain = new DataInputStream(sk1.getInputStream());
			Handler_Reader = new BufferedReader(new InputStreamReader(Handler_Datain));
			
			// setup Handler_Output
			Handler_Dataout = new DataOutputStream(sk1.getOutputStream());
			Handler_Writer = new BufferedWriter(new OutputStreamWriter(Handler_Dataout));
			
			// get Handler_SocketName
			Handler_SocketName = Handler_Reader.readLine();
			
			System.out.println(Handler_SocketName + " connected successfully! currently online: " + Handler_Sockets.size());
			Handler_Broadcastall(Handler_SocketName + " has joined! (currently online: " + Handler_Sockets.size() + ")");
		} catch (IOException e) {
			closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
		}
	}
	
	public void Handler_ReceiveMsg() {
		Thread thread_receiveMsg = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msgReceived;
					
					while (Handler_Socket.isConnected()) {
						msgReceived = Handler_Reader.readLine();
						Handler_Broadcastall("[" + Handler_SocketName + "]: " + msgReceived);
					}
				} catch (IOException e) {
//					e.printStackTrace();
//					closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
				}
			}
		});
		
		thread_receiveMsg.start();
	}
	
//	public void Handler_Broadcast(String MsgToBroad) {
//		for (Handler i: Handler_Sockets) {
//			try {
//				if (!i.Handler_SocketName.equals(Handler_SocketName)) {
//					i.Handler_Writer.write(MsgToBroad);
//					i.Handler_Writer.newLine();
//					i.Handler_Writer.flush();
//				}
//			} catch (IOException e) {
//				closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
//			}
//		}
//	}
	
	
	public void Handler_Broadcastall(String MsgToBroad) {
		for (Handler i: Handler_Sockets) {
			try {
				i.Handler_Writer.write(MsgToBroad);
				i.Handler_Writer.newLine();
				i.Handler_Writer.flush();					
			} catch (IOException e) {
//				e.printStackTrace();
				closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
				break;
			}
		}
	}
	
	public void removeSocket() {
		Handler_Sockets.remove(this);
	}
	
	public void closeEverything(Socket sk1, DataInputStream datain1, DataOutputStream dataout1, BufferedReader bfread1, BufferedWriter bfwrite1) {
		try {
			removeSocket();
			
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
			
			System.out.println(Handler_SocketName + " disconnected successfully! currently online: " + Handler_Sockets.size());
			Handler_Broadcastall(Handler_SocketName + " has left! (currently online: " + Handler_Sockets.size() + ")");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}