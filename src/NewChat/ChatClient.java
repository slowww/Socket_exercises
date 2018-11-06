package NewChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ChatClient {

	private final String serverName;
	private final int serverPort;
	private Socket socket;
	private OutputStream versoServer;
	private InputStream dalServer;
	private BufferedReader bufferdalServer;
	
	private ArrayList<UserStatusListener> userStatuslisteners = new ArrayList<>();//per ogni utente c'è un "ascoltatore" che 
	//ne verifica lo stato quindi faccio una lista
	
	private ArrayList<MsgListener> msglisteners = new ArrayList<>();
	
	
	//costruttore
	public ChatClient(String serverName, int serverPort)
	{
		this.serverName = serverName;
		this.serverPort = serverPort;
	}
	
	
	public void addUserstatuslistener (UserStatusListener listener)
	{
		userStatuslisteners.add(listener);
	}
	
	public void removeUserstatuslistener (UserStatusListener listener)
	{
		userStatuslisteners.remove(listener);
	}
	
	
	
	
	public void addMsglistener (MsgListener listener)
	{
		msglisteners.add(listener);
	}
	
	public void removeMsglistener (MsgListener listener)
	{
		msglisteners.remove(listener);
	}
	
	
	
	
	
	
	
	public boolean connect()
	{
		try {
			this.socket = new Socket(serverName,serverPort);
			this.versoServer = socket.getOutputStream();
			this.dalServer = socket.getInputStream();
			this.bufferdalServer = new BufferedReader(new InputStreamReader(dalServer));
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean login (String login, String password) throws IOException
	{
		String cmd = "login " + login + " " + password + "\n";
		versoServer.write(cmd.getBytes());
		
		String rispServer = bufferdalServer.readLine();
		System.out.println("SERVER: " + rispServer);
		
		if("Ok login".equalsIgnoreCase(rispServer))
		{
			startLettoremsg();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	private void gestoredegliOnline(String[] tokens) {
		String login = tokens[1];
		
		for(UserStatusListener listener : userStatuslisteners)
		{
			listener.online(login);
		}	
		
	}
	
	private void gestoredegliOffline(String[] tokens) {
		String login = tokens[1];
		
		for(UserStatusListener listener : userStatuslisteners)
		{
			listener.offline(login);
		}	
		
	}
	
	
	private void lettoreLoopmsg() 
	{
		
		try {
			String line;
			
			while((line = bufferdalServer.readLine())!= null)
			{
				String[] tokens = split(line);
				
				if(tokens != null && tokens.length > 0)
				{
					String cmd = tokens[0];
					
					if("online".equalsIgnoreCase(cmd))
					{
						gestoredegliOnline(tokens);
					}
					else
					{
						if("offline".equalsIgnoreCase(tokens))
						{
							gestoredegliOffline(tokens);
						}
					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}	
	
	
	
	



	private void startLettoremsg() {
		
		Thread t = new Thread()
		{
			public void run()
			{
				lettoreLoopmsg();
			}
			
			
		};
		
		t.start();
		
	}

	
	private void logoff() {
		String cmd = "logoff \n";
		versoServer.write(cmd.getBytes());
		
	}
	
	private void msgcltocl(String dest, String contenuto)
	{
		String cmd = "msg " + " " + dest + " " + contenuto + "\n";
		dalServer.write(cmd.getBytes());
	}


	//------------------------MAIN-----------------------------
	public static void main(String[] args) throws IOException {
		
		ChatClient client = new ChatClient("localhost", 8818);
		
		client.addUserstatuslistener(new UserStatusListener()
				{
					public void online (String login)
					{
						System.out.println(login + " ONLINE!\n");
					}
					public void offline (String login)
					{
						System.out.println(login + " OFFLINE!\n");
					}
				}

				
				);
		
		
		client.connect();
		
		if(client.connect())
		{
			System.out.println("CONNESSO!");
			client.login("guest","password");
			
			client.msgcltocl("Dario","Hey"); //chiaramente, per ricevere questo messaggio uno dei due deve loggarsi come Dario
		}
		else
		{
			System.err.println("Connessione non riuscita :-(");
		}
		
		client.logoff();
	}



}
