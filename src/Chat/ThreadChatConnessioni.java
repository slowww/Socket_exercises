package Chat;

import java.io.*;
import java.net.Socket;



//Questa classe viene associata ad ogni client connesso con il server
public class ThreadChatConnessioni implements Runnable
{
	//DICHIARAZIONE VARIABILI
	private ThreadGestioneServizioChat gestoreChat;
	private Socket client = null;
	private BufferedReader input = null;
	private PrintWriter output = null;
	Thread me;
	
	//ogni volta che un client si connette al server si crea un oggetto ThreadChatConnessioni
	//che crea gli stream di I/O e contiene un metodo per spedire un messaggio al singolo client
	public ThreadChatConnessioni(ThreadGestioneServizioChat gestoreChat, Socket client)	//COSTRUTTORE
	{
		this.gestoreChat = gestoreChat;
		this.client = client;
		
		try {
			this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.output = new PrintWriter(this.client.getOutputStream(),true);//true per indicare che il contenuto è flushable
		} catch (IOException e) {
			output.println("Errore nella lettura");
		}
		
		me = new Thread(this);//creo (ad ogni richiesta di connessione client al server) un nuovo ThreadChatConnessioni
		me.start();//il metodo start invoca il metodo run dell'interfaccia runnable (non possiamo invocare direttamente il run!)
		
	}
	
	
	
	
	
	//...il metodo run...
	public void run()
	{
			while(true)
			{
				try
				{
					String mex = null;
					
					while ((mex=input.readLine()) == null)
					{
						//finchè il messaggio in arrivo dal server è vuoto..non fare niente.
					}
					
					gestoreChat.spedisciMessaggio(mex);//..altrimenti prendi il messaggio e spediscilo a tutti i client
				}catch(Exception e)
				{
					output.println("Errore nella spedizione del messaggio a tutti!");
				}
			}
	}
	
	
	
	
	
	
	
	
		
		
	public void spedisciMessaggioChat(String messaggio)//spedisci messaggio verso il server
	{
		    try{
		      output.println(messaggio);
		    }catch(Exception e){
		      output.println("Errore nella spedizione del singolo messaggio.");
		    }           
	}
		
		
		
	}
}
