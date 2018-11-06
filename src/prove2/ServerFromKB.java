package prove2;
import java.io.*;
import java.net.*;

public class ServerFromKB {
	
	ServerSocket serverS = null;
	Socket ClientWaiter = null;
	String stringaRicevuta = null;
	String stringaMod = null;
	BufferedReader inDalClient;
	DataOutputStream outVersoClient;
	
	public Socket Attendi()
	{
		try {
			System.out.println("SERVER partito e in esecuzione...");
			//creo un server sulla porta 6789
			serverS = new ServerSocket(6789);
			
			//rimane in attesa di un client...
			ClientWaiter = serverS.accept();
			
			//chiudo il server per inibire altri client
			serverS.close();
			
			//soliti strumenti per ricevere e inviare da-verso il client
			inDalClient = new BufferedReader(new InputStreamReader(ClientWaiter.getInputStream()));
			outVersoClient = new DataOutputStream(ClientWaiter.getOutputStream());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Errore durante l'istanza del server!");
			System.exit(1);
		}
		
		return ClientWaiter;

	}
	
	public void Comunica()
	{
		try {
			System.out.println("Benvenuto, Client! Scrivi una frase e la trasformo in maiuscolo. Attendo...");
			stringaRicevuta = inDalClient.readLine();
			
			System.out.println("La stringa ricevuta è: " + stringaRicevuta);
			
			stringaMod = stringaRicevuta.toUpperCase();
			System.out.println("Invio della stringa modificata al client...");
			outVersoClient.writeBytes(stringaMod);
			
			System.out.println("Fine elaborazione! Chiudo il socket! Bye bye!");
			ClientWaiter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerFromKB servente = new ServerFromKB();
		servente.Attendi();
		servente.Comunica();

	}

}
