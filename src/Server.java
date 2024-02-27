import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket Server_ServerSocket;
	
	public Server(ServerSocket svSocketValue1) {
		Server_ServerSocket = svSocketValue1;
	}
	
	public void Server_Start() {
		try {
			while (!Server_ServerSocket.isClosed()) {
				Socket Server_Client = Server_ServerSocket.accept();
				
				Handler HandlerObj = new Handler(Server_Client);
				HandlerObj.Handler_ReceiveMsg();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Server_Shutdown();
		}
	}
	
	public void Server_Shutdown() {
		try {
			Server_ServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			ServerSocket Server001 = new ServerSocket(51002);
			
			Server ServerObj = new Server(Server001);
			
			System.out.println("Server started! Waiting for connection...");
			ServerObj.Server_Start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}