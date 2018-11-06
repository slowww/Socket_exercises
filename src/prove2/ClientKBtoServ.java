package prove2;
import java.io.*;
import java.net.*;


public class ClientKBtoServ {
	//DICHIARAZIONE VARIABILI comuni a tutta la classe
	String nomeServer = "localhost";
	int portaServer = 6789;
	
	Socket miosocket; 
	
	DataOutputStream outVersoServer;
	
	BufferedReader bufferKB;
	BufferedReader dalServer;
	
	String stringaUtente;
	String stringaRicevutaDalServer;
	
	

	
	
	
	
	
	
	
	
	
	public Socket siConnette() //metodo per connettermi al server (infatti creo un  socket alla fine)
	{
		System.out.println("CLIENT partito in esecuzione...");
		
		try {
			//ASSEGNAZIONE VARIABILI
			bufferKB = new BufferedReader(new InputStreamReader(System.in));//inputstreamreader serve per
			//leggere il flusso di dati da tastiera a "noi"
			
			miosocket = new Socket (nomeServer,portaServer);//istanzio il socket con i dati del server al quale voglio connettermi
			
			outVersoServer = new DataOutputStream(miosocket.getOutputStream());//creo il flusso sul quale dovranno
			//"viaggiare" i dati dal mio client al server (recupero questi dati applicando il metodo getouputstream al socket)
			//new *creo l'output stream*(*i dati da scrivere sull'output stream*)
			//NB: NON ESISTE UN "OUTPUTSTREAMREADER"
			
			dalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
			//recupero quanto inviato dal server con miosocket.getinputstream()
			//leggo (un byte alla volta) con inputstreamreader
			//raccolgo in "blocchi" quanto recuperato da inputstreamreader attraverso il bufferedreader
			
		} catch (UnknownHostException e) {
			System.err.println("Host sconosciuto!");
		}
		catch (Exception e)
		{		
			System.out.println(e.getMessage());
			System.out.println("Errore durante la connessione!");
			System.exit(1);
		}		
		
		return miosocket; //questo metodo è di tipo Socket perchè ritorna un oggetto socket. Ritorna un oggetto
		// di tipo Socket perchè stiamo costruendo un CLIENT.
		//Un client, in quanto tale, durante la creazione di un socket verso un server DEVE specificare
		//a quale server connettersi specificando PORTA e IP (o nome host) e lo fa appunto utilizzando 
		//un'istanza della classe socket dove appunto specifica porta e ip!
		
	}
	
	public void comunica()//metodo per comunicare con il server (inviare e ricevere stringa)
	{
		try {
			//leggo una riga
			System.out.println("Inserisci la stringa da trasmettere al server: ");
			stringaUtente = bufferKB.readLine();
			
			//la spedisco al server
			System.out.println("...Invio la stringa al server e attendo...");
			outVersoServer.writeBytes(stringaUtente+'\n');
			
			//leggo la risposta dal server
			stringaRicevutaDalServer = dalServer.readLine();
			System.out.println("Questa è la risposta del server: " + stringaRicevutaDalServer);
			
			//chiudo la connessione
			System.out.println("CLIENT: termina elaborazione e chiude connessione");
			miosocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Errore durante la comunicazione col server!");
			System.exit(1);
		}
	}
	
	public static void main (String args[])
	{
		ClientKBtoServ cliente = new ClientKBtoServ();
		cliente.siConnette();//ovviamente prima instauro la connessione
		cliente.comunica();//...e poi comunico :)
	}
	
}

