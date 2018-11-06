package NewChat;
import java.util.*;
import java.io.*;
import java.net.*;

public class ServerMain { //questo è il programma principale del server dove si richiamano le funzioni 
	//necessarie alla gestione di tutto [modularizzazione in piu blocchi di funzione]

	public static void main(String[] args) {
		
		int port = 8818;
		
		Server server = new Server(port);

		server.start();//faccio partire il thread Server
		
		//Server all'interno del suo run() farà partire ServerWorker [worker.start()]
		
		//ServerWorker all'interno del suo run() fa partire gestoreClient() (che si trova dentro a ServerWorker)
		
		//gestoreClient() richiama al suo interno la funzione gestisciLogin()

	}

}
