package NewChat;

import org.omg.CORBA_2_3.portable.OutputStream;

import java.util.*;
import java.io.*;
import java.net.*;


public class ServerWorker extends Thread
{
	private final Socket clientSocket;
	private final Server server;
	private String login = null;
	private OutputStream outputstream;
	private HashSet<String> topicSet = new HashSet<>();
	//un hashset è una collezione (di hash #) in cui gli elementi 
	//compaiono senza ripetizioni, e non sono organizzati in un ordine prefissato.
	
	
	
	
	//includiamo Server nel costruttore di ServerWorker 
	//per poter chiamare i metodi di Server all'interno della classe ServerWorker
	//perchè tutti i ServerWorker connessi al server ad esempio possano essere messi al corrente 
	//dei nuovi eventuali login
	public ServerWorker(Server server, Socket clientSocket)//costruttore
	{
		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	
	
	
	
////----------------METODI GESTIONE DEI TOPIC------------------------------------------------/////
	public boolean contieneTopic(String topic)
	{
		return topicSet.contains(topic);//TRUE=il topic è contenuto nel set, FALSE=...
	}
	
	
	public void gestoreJoin(String[] tokens)//entro nel topic
	{
		if(tokens.length > 1)
		{
			String topic = tokens[1];
			topicSet.add(topic);
		}
	}
	
	
	public void gestoreLeave(String[] tokens)//esco dal topic
	{
		if(tokens.length > 1)
		{
			String topic = tokens[1];
			topicSet.remove(topic);
		}
	}
//////---------------------FINE METODI GESTIONE DEI TOPIC-------------------------------/////
	
	
	
	
	
	
	
	
	private void gestoreClient()
	//gestisce lo scambio di stringhe con il client E BASTA
	//per gestire il login c'è il metodo gestoreLogin() piu in basso	
	{
		this.outputstream = clientSocket.getOutputStream();//quello che il server manda al client
		//NOTA: ho dovuto associare l'output stream all'oggetto ServerWorker (mettendo il this) per rendere il flusso
		//di dati verso il client condiviso tra i vari metodi della classe: in questo modo posso utilizzarlo nel
		//metodo Send
		
		InputStream inputstream = clientSocket.getInputStream();//quello che il client manda al server
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
		
		/*for(int i=0; i<10; i++)
		{
			outputstream.write(("questa è la mia bestemmia numero " + i).getBytes());//getBytes trasforma la stringa in bytes
											//rendendola leggibile da outputStream
		}*/
		
		String stringadalclient = reader.readLine();
		
		//String messaggioperclient = null;
				
		while(stringadalclient != "bona")
		{
			String[] tokens = stringadalclient.split("");//controllare carattere split!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			if(tokens != null && tokens.length > 0)
			{
				String cmd = tokens[0];//controllo la prima parola inserita 
			
					if("bona".equalsIgnoreCase(cmd) || "logoff".equalsIgnoreCase(cmd))
					{
						gestoreLogoff();
						break;
					}
					else
					{
						if("login".equalsIgnoreCase(cmd))
						{
							gestisciLogin(outputstream,tokens);
						}
						else
						{
							if("Msg".equalsIgnoreCase(cmd))
							{
								
								String[] tokensmsg.split("");//controllare!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
								gestoreMessaggi(tokensmsg);
							}
							else
							{	if("join".equalsIgnoreCase(cmd))
								{
									gestoreJoin(tokens);
								}
								else
								{
									if("leave".equalsIgnoreCase(cmd))
									{
										gestoreLeave(tokens);
									}
									else
									{
										String msg = "Comando " + tokens + " non riconosciuto!";
										outputstream.write(msg.getBytes());
									}
								}
								
							}
						}
						
						
					}
					
			}
			//messaggioperclient = "Hai scritto: " + stringadalclient;
			//outputstream.write(messaggioperclient.getBytes());
		}
		
		clientSocket.close();
	}

	
	
	
	
		
	
	public String getLogin()
	{
		return login;
	}

	
	
	private void gestoreLogoff() throws IOException
	{
		server.rimuoviWorker(this);
		List<ServerWorker> workerList = server.getWorkerList();
		
		String offlineMsg = login + " è offline!\n";
		
		//comunica a tutti gli user che l'user corrente è offline
		for (ServerWorker worker : workerList)
		{
			if(!login.equals(worker.getLogin()))
			{
				worker.Send(offlineMsg);
			}
		}
		
		clientSocket.close();
		
	}
	
	
	
	
	
	
	
	
	
	

	private void gestisciLogin (OutputStream outputstream, String[] tokens)
	//controlla l'inserimento di user e password da parte del client
	//e gli comunica se è andato a buon fine oppure no
	{
		List<ServerWorker> workerList = server.getWorkerList();//lista di oggetti di classe ServerWorker
		//ovvero la lista di tutti i client connessi al server

		
		if(tokens.length == 3)//se la lunghezza dell'array è 3 significa 
			//che l'utente ha inserito correttamente la sequenza user e password 
		{
			String login = tokens[1];
			String password = tokens[2];
			
			if( (login.equals("guest") && password.equals("guest") ) || (login.equals("dario") && password.equals("password")))
			{
				String messaggioperclient = "Ok login\n";
				try {
					outputstream.write(messaggioperclient.getBytes());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				this.login = login;
				
				System.out.println(login + " loggato con successo!\n");
				
				String onlineMsg = "L'utente " + login + " è online!\n";
				String onlineMsg2 = login + " è tra gli utenti in linea!\n";
				
				
				
				/*comunica a tutti gli utenti connessi alla chat, la lista di tutti gli user connessi
				//tranne il proprio!
				for (ServerWorker worker : workerList)
				{
					
						if(worker.getLogin() != null)
						{
							worker.Send(onlineMsg2);
						}
					
					
				}*///CONTROLLARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				//comunica a tutti gli user che l'user corrente è online
				for (ServerWorker worker : workerList)
				{
					if(!login.equals(worker.getLogin()))
					{
						try {
							worker.Send(onlineMsg);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		else
		{
			String messaggioperclient = "Errore nel login!\n";
			try {
				outputstream.write(messaggioperclient.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
		}
	}
		
		
		
		
	public void Send(String msg) throws IOException
	{
		outputstream.write(msg.getBytes());
	}
		
		
		
	//FORMATO: "msg *nome utente destinatario [1]* *contenuto del messaggio[2]*"	
	public void gestoreMessaggi(String[] tokens) throws IOException
	{
		String dest = tokens[1];
		String contenuto = tokens[2];
		
		boolean isTopic = dest.charAt(0) == '#';//il secondo token (dest) è un topic se il primo carattere è un #
		//quindi eventualmente dest diventa la variabile che contiene il nome del topic
		
		List<ServerWorker> workerList = server.getWorkerList();
		
		for (ServerWorker worker : workerList)
		{
			if(isTopic)
			{
				if (worker.contieneTopic(dest)) 
				{
					String msginuscita = "Msg da " + login + " per il topic " + dest + ": " + contenuto + "\n";
					worker.Send(msginuscita);
				}
			}
			else
			{
				if(dest.equalsIgnoreCase(worker.getLogin()))
				{
					String msginuscita = "Msg da " + login + ": " + contenuto + "\n";
					worker.Send(msginuscita);
				}
			}
		}
	}
		
		
	
		
	public void run()
	{
		gestoreClient();
	}
}
