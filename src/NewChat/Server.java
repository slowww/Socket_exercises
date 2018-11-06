package NewChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread
{
	private final int serverPort;
	private ArrayList<ServerWorker> workerList = new ArrayList<>();
	
	
	//costruttore
	public Server (int serverPort)
	{
		this.serverPort = serverPort;
	}

	
	
	
	
	
	
	
	public List<ServerWorker> getWorkerList()
	{
		return workerList;
	}
	
	
	
	
	public void rimuoviWorker(ServerWorker serverWorker)
	{
		workerList.remove(serverWorker);
	}
	
	
	
	
	public void run ()
	{
		try {
				ServerSocket serverSocket = new ServerSocket(serverPort);
			
				while(true)//loop: continua a creare dei thread di ascolto sul socket
				{
					Socket clientSocket = serverSocket.accept();
					ServerWorker worker = new ServerWorker(this, clientSocket);
				
					worker.start();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
}
