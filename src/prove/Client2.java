package prove;
import java.io.*;
import java.net.*;

//public Socket (String host, int port) throws IOException;

public class Client2 {
	
	String nomeServer = "nomeServer";
	int portaServer = 6789;
	DataInputStream in;
	DataOutputStream out;
	
	protected Socket connetti() throws IOException
	{
		Socket socket = new Socket (nomeServer,portaServer);
		
		out = new DataOutputStream (socket.getOutputStream());
		in = new DataInputStream (socket.getInputStream());
		
		return socket;
	}
	

}